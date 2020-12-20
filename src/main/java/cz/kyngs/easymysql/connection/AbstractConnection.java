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

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractConnection {

    protected final Connection connection;

    public AbstractConnection(Connection connection) {
        this.connection = connection;
    }

    public AbstractConnection(HikariDataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

}
