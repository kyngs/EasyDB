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

package cz.kyngs.easymysql.connection.async;

import cz.kyngs.easymysql.utils.ThrowableConsumer;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class AsyncThreadWorker {

    private final AsyncConnection master;
    private final Thread thread;

    public AsyncThreadWorker(AsyncConnection master, int id) throws SQLException {
        this.master = master;
        thread = new Thread(this::run, String.format("EasyMySQL Worker #%d", id));
        thread.start();
    }

    public void run() {
        while (true) {
            try (Connection connection = this.master.getDataSource().getConnection()) {
                ThrowableConsumer<Connection, Exception> consumer = this.master.getWork();
                consumer.accept(connection);
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                LoggerFactory.getLogger(AsyncThreadWorker.class).warn("An error occurred while performing async job.", e);
            }
        }
    }

    public void stop() {
        thread.interrupt();
    }

}
