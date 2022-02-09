/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb;

public class ConnectionException extends RuntimeException {

    private final Exception cause;

    public ConnectionException(Exception cause) {
        this.cause = cause;
    }

    @Override
    public Exception getCause() {
        return cause;
    }
}
