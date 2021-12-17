/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.provider;

import xyz.kyngs.easydb.EasyDB;
import xyz.kyngs.easydb.scheduler.ThrowableFunction;

public interface Provider<T, E extends Throwable> {

    /**
     * Starts the provider and its dependencies
     *
     * @param easyDB API main
     */
    void start(EasyDB easyDB);

    /**
     * Tells the provider to start accepting requests
     */
    void open();

    /**
     * Tells the provider to stop accepting requests
     */
    void close();

    /**
     * Stops the provider and its dependencies
     * This should be done after all waiting requests have been executed
     */
    void stop();

    boolean isOpen();

    <V> V runTask(ThrowableFunction<T, E> task) throws E;

}
