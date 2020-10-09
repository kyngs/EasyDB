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

import java.sql.SQLException;

public class MySQLBuilder extends HikariConfig {

    private int threadCount;

    public MySQLBuilder(){

        threadCount = 4;
        setJdbcUrl("jdbc:mysql://localhost:3306/");
        setUsername("root");
        addDataSourceProperty( "cachePrepStmts" , "true" );

    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public MySQL build() throws SQLException {
        return new MySQL(this, threadCount);
    }

    public void setHostAndPort(String hostname){
        setJdbcUrl(String.format("jdbc:mysql://%s/", hostname));
    }

}
