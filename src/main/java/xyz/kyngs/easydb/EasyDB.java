/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb;

import xyz.kyngs.easydb.provider.Provider;
import xyz.kyngs.easydb.scheduler.Scheduler;
import xyz.kyngs.easydb.scheduler.ThrowableConsumer;
import xyz.kyngs.easydb.scheduler.ThrowableFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public class EasyDB {

    private static Scheduler globalScheduler;

    private final Scheduler scheduler;
    private final Map<Class<? extends Provider>, Provider> providers;

    public EasyDB(EasyDBConfig config) {
        config.build();
        if (config.useGlobalScheduler) {
            if (globalScheduler == null) globalScheduler = new Scheduler(config.executor);
            scheduler = globalScheduler;
        } else {
            scheduler = new Scheduler(config.executor);
        }

        providers = new HashMap<>();

        for (Provider provider : config.providers) {
            providers.put(provider.getClass(), provider);
        }

        dispatchTask(provider -> provider.start(this));

        dispatchTask(Provider::open);

    }

    public <T extends Provider> T getProvider(Class<T> clazz) throws ClassCastException {
        //noinspection unchecked
        return (T) providers.get(clazz);
    }

    private <X extends Throwable, E, T extends Provider<E, X>, V> V runTask(ThrowableFunction<E, X> task, Class<T> clazz) {
        try {
            return getProvider(clazz).runTask(task);
        } catch (Throwable e) {
            System.err.println("[EasyDB] An error occurred while performing EasyDB job.");
            throw new RuntimeException(e);
        }
    }

    public <X extends Throwable, E, T extends Provider<E, X>> void runTaskSync(ThrowableConsumer<E, X> task, Class<T> clazz) {
        runTask(task, clazz);
    }

    private <X extends Throwable, E, T extends Provider<E, X>> void runTask(ThrowableConsumer<E, X> task, Class<T> clazz) {
        runTask(new ThrowableFunction<>() {
            @Override
            public <V> V run(E e) throws X {
                task.run(e);
                return null;
            }
        }, clazz);
    }

    public <X extends Throwable, E, T extends Provider<E, X>> void runTaskAsync(ThrowableConsumer<E, X> task, Class<T> clazz) {
        scheduler.schedule(() -> runTask(task, clazz));
    }

    public <X extends Throwable, E, T extends Provider<E, X>, V> V runFunctionSync(ThrowableFunction<E, X> task, Class<T> clazz) {
        return runTask(task, clazz);
    }

    private void dispatchTask(Consumer<Provider> task) {
        for (Provider provider : providers.values()) {
            task.accept(provider);
        }
    }

    public void stop() {
        dispatchTask(Provider::close);

        scheduler.stop();

        dispatchTask(Provider::stop);
    }

}
