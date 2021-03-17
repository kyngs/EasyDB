/*
 * Copyright (c) 2021 kyngs
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

import java.sql.SQLException;

public class MySQLBuilder extends HikariConfig {

    private int threadCount;
    private String host;
    private int port;
    private String database;
    private boolean autoReconnect;

    public MySQLBuilder(){

        threadCount = 4;
        setJdbcUrl("jdbc:mysql://localhost:3306/");
        setUsername("root");
        addDataSourceProperty("cachePrepStmts", "true");
        host = "localhost";
        port = 3306;
        database = "";
        autoReconnect = true;

    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public MySQL build() throws SQLException {
        setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s%s", host, port, database, autoReconnect ? "?autoReconnect=true" : ""));
        return new MySQL(this, threadCount);
    }

    public void setHostAndPort(String hostname){
        setJdbcUrl(String.format("jdbc:mysql://%s/", hostname));
    }

}
