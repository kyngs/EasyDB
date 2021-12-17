/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.provider;

import xyz.kyngs.easydb.scheduler.ThrowableFunction;

public abstract class AbstractProvider<T, E extends Throwable> implements Provider<T, E> {

    private boolean open;

    @Override
    public void close() {
        open = false;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public <V> V runTask(ThrowableFunction<T, V, E> task) {
        if (!open) throw new IllegalStateException("Provider closed");
        return null;
    }

    @Override
    public void open() {
        open = true;
    }
}
