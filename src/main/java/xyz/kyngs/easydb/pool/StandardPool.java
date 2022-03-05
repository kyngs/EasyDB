/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class StandardPool<T> implements Pool<T> {

    private final BlockingQueue<T> free, occupied;
    private final DryBehavior behavior;
    private final LimitReachBehavior limitReachBehavior;
    private final Supplier<T> supplier;
    private final Consumer<T> destroyer;
    private final Function<T, Boolean> healthCheck;
    private final int maxSize;
    private final Thread finalizer;
    private final AtomicInteger size;
    private volatile boolean open;

    public StandardPool(DryBehavior behavior, LimitReachBehavior limitReachBehavior, Supplier<T> supplier, Consumer<T> destroyer, Function<T, Boolean> healthCheck, int initialSize, int maxSize) {
        this.behavior = behavior;
        this.limitReachBehavior = limitReachBehavior;
        this.supplier = supplier;
        this.destroyer = destroyer;
        this.healthCheck = healthCheck;
        this.maxSize = maxSize;
        free = new LinkedBlockingQueue<>();
        occupied = new LinkedBlockingQueue<>();
        size = new AtomicInteger(0);
        open = true;

        finalizer = new Thread(() -> {
            while (occupied.size() > 0) {
                synchronized (occupied) {
                    try {
                        occupied.wait();
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
            }

            free.forEach(destroyer);
        }, "Pool Finalizer");

        for (int i = 0; i < initialSize; i++) {
            free.add(newOne());
        }
    }

    private T newOne() throws IndexOutOfBoundsException {
        size.updateAndGet(operand -> {
            if (operand >= maxSize) throw new IndexOutOfBoundsException();

            return operand + 1;
        });

        try {
            return supplier.get();
        } catch (RuntimeException | Error e) {
            size.decrementAndGet();
            throw e;
        }
    }

    @Override
    public int close() {
        open = false;

        finalizer.start();

        return occupied.size();
    }

    @Override
    public void awaitClose() throws InterruptedException {
        finalizer.join();
    }

    @Override
    public T obtain() throws InterruptedException {
        openCheck();
        var t = free.poll();

        if (t == null) {
            t = switch (behavior) {
                case NULL -> null;
                case WAIT -> waitFor();
                case POPULATE -> {
                    try {
                        yield newOne();
                    } catch (IndexOutOfBoundsException e) {
                        yield switch (limitReachBehavior) {
                            case WAIT -> waitFor();
                            case NULL -> null;
                        };
                    }
                }
            };
        }

        if (t != null && !healthCheck.apply(t)) {
            destroyer.accept(t);

            return obtain();
        }

        if (t != null) occupied.add(t);

        return t;

    }

    private T waitFor() throws InterruptedException {
        return free.take();
    }

    @Override
    public void cycle(T t) {
        if (!occupied.remove(t)) throw new IllegalStateException();

        if (!healthCheck.apply(t)) {
            destroyer.accept(t);
            return;
        }

        free.add(t);

        if (!open) {
            synchronized (occupied) {
                occupied.notifyAll();
            }
        }
    }

    private void openCheck() {
        if (!open) throw new IllegalStateException("The pool has already been closed");
    }
}
