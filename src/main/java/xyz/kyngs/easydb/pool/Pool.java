/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.pool;

public interface Pool<T> {

    int close();

    void awaitClose() throws InterruptedException;

    T obtain() throws InterruptedException;

    void cycle(T t);

}
