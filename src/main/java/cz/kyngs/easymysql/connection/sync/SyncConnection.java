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

package cz.kyngs.easymysql.connection.sync;

import com.zaxxer.hikari.HikariDataSource;
import cz.kyngs.easymysql.connection.AbstractConnection;
import cz.kyngs.easymysql.utils.ThrowableConsumer;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class SyncConnection extends AbstractConnection {

    public SyncConnection(HikariDataSource hikariDataSource) {
        super(hikariDataSource);
    }

    @Override
    public void schedule(ThrowableConsumer<Connection, Exception> task) {
        try (Connection connection = getDataSource().getConnection()) {
            task.accept(connection);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).warn("An error occurred while performing sync job.", e);
        }
    }


}
