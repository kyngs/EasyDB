/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb;

import xyz.kyngs.easydb.provider.Provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class EasyDBConfig<P extends Provider<T, E>, T, E extends Exception> {

    protected final P provider;

    /**
     * Makes EasyDB use one shared Scheduler  per VM, instead of creating a new one with each instance, useful in plugin systems.
     * Enabled by default.
     */
    protected boolean useGlobalScheduler;
    protected ExecutorService executor;

    /**
     * If function returns true, the exception will be thrown, logged if async.
     * Connection exceptions won't be handled by exceptionHandler (if identified correctly)
     * All throwables that do not extend {@link Exception} will not be caught.
     */
    protected Function<Exception, Boolean> exceptionHandler, connectionExceptionHandler;

    public EasyDBConfig(P provider) {
        this.provider = provider;
        useGlobalScheduler = true;
    }

    public EasyDBConfig<P, T, E> setExceptionHandler(Function<Exception, Boolean> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public EasyDBConfig<P, T, E> setConnectionExceptionHandler(Function<Exception, Boolean> connectionExceptionHandler) {
        this.connectionExceptionHandler = connectionExceptionHandler;
        return this;
    }

    protected void build() {
        if (executor == null) executor = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors());
        if (exceptionHandler == null) exceptionHandler = e -> true;
        if (connectionExceptionHandler == null) connectionExceptionHandler = e -> true;
    }

    public EasyDBConfig<P, T, E> useGlobalExecutor(boolean use) {
        this.useGlobalScheduler = use;
        return this;
    }

    public EasyDBConfig<P, T, E> setExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }
}
