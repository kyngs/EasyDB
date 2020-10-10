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

import cz.kyngs.easymysql.connection.AbstractConnection;
import cz.kyngs.easymysql.utils.ThrowableConsumer;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class SyncConnection extends AbstractConnection {
    public SyncConnection(Connection connection) {
        super(connection);
    }

    public Connection get(){
        return connection;
    }

    public void schedule(ThrowableConsumer<Connection, SQLException> consumer){
        try {
            consumer.accept(connection);
        }catch (Exception e){
            LoggerFactory.getLogger(getClass()).warn("An error occurred while performing sync job.", e);
        }
    }

}
