/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.scheduler;

public interface ThrowableFunction<T, V, E extends Throwable> {

    V run(T t) throws E;

}
