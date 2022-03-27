/*
 * Copyright (c) 2022 kyngs
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

import java.util.function.Function;

public class EasyDB<P extends Provider<T, E>, T, E extends Exception> {

    private static Scheduler globalScheduler;

    private final Scheduler scheduler;
    private final P provider;
    private final Function<Exception, Boolean> exceptionHandler, connectionExceptionHandler;
    public EasyDB(EasyDBConfig<P, T, E> config) {
        config.build();

        this.exceptionHandler = config.exceptionHandler;
        this.connectionExceptionHandler = config.connectionExceptionHandler;

        if (config.useGlobalScheduler) {
            if (globalScheduler == null) globalScheduler = new Scheduler(config.executor);
            scheduler = globalScheduler;
        } else {
            scheduler = new Scheduler(config.executor);
        }

        this.provider = config.provider;

        provider.start(this);

        provider.open();

    }

    public P getProvider() {
        return provider;
    }

    private <V> V runTask(ThrowableFunction<T, V, E> task) {
        try {
            return provider.runTask(task);
        } catch (ConnectionException e) {
            var cause = e.getCause();
            if (connectionExceptionHandler.apply(cause)) throwExceptionAsRuntime(cause);
        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                if (e.getCause() instanceof Exception cause) {
                    handleException(cause);
                    return null;
                } else {
                    throw e;
                }
            }

            handleException(e);
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    private void handleException(Exception e) {
        var bool = provider.identifyConnectionException(e);

        if (bool ? connectionExceptionHandler.apply(e) : exceptionHandler.apply(e)) throwExceptionAsRuntime(e);
    }

    private void throwExceptionAsRuntime(Exception e) {
        if (e.getCause() instanceof RuntimeException ex) throw ex;

        if (e instanceof RuntimeException ex) throw ex;
        else throw new RuntimeException(e);

    }

    public void runTaskSync(ThrowableConsumer<T, E> task) {
        runTask(task);
    }

    private void runTask(ThrowableConsumer<T, E> task) {
        runTask(e -> {
            task.run(e);
            return null;
        });
    }

    public void runTaskAsync(ThrowableConsumer<T, E> task) {
        scheduler.schedule(() -> {
            try {
                runTask(task);
            } catch (Exception e) {
                System.err.println("[EasyDB] Exception on async task");
                e.printStackTrace();
            }
        });
    }

    public <V> V runFunctionSync(ThrowableFunction<T, V, E> task) {
        return runTask(task);
    }

    public void stop() {
        provider.close();

        scheduler.stop();

        provider.stop();
    }

}
