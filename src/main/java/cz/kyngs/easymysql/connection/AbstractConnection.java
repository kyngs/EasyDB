/*
 * Copyright (c) 2020 kyngs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 */

package cz.kyngs.easymysql.connection;

import com.zaxxer.hikari.HikariDataSource;
import cz.kyngs.easymysql.utils.ThrowableConsumer;

import java.sql.Connection;

public abstract class AbstractConnection {

    protected final HikariDataSource hikariDataSource;

    public AbstractConnection(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    public HikariDataSource getDataSource() {
        return hikariDataSource;
    }

    public abstract void schedule(ThrowableConsumer<Connection, Exception> task);

}
