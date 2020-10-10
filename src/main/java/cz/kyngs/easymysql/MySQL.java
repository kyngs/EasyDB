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

package cz.kyngs.easymysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cz.kyngs.easymysql.connection.async.AsyncConnection;
import cz.kyngs.easymysql.connection.sync.SyncConnection;

import java.sql.SQLException;

public class MySQL {

    private final AsyncConnection async;
    private final SyncConnection sync;
    private final HikariDataSource hikariDataSource;

    protected MySQL(HikariConfig hikariConfig, int threadCount) throws SQLException {

        hikariDataSource = new HikariDataSource(hikariConfig);

        async = new AsyncConnection(hikariDataSource.getConnection(), threadCount);
        sync = new SyncConnection(hikariDataSource.getConnection());

    }

    public AsyncConnection async(){
        return async;
    }

    public SyncConnection sync(){
        return sync;
    }

    public void close(){
        async.close();
        hikariDataSource.close();
    }

}
