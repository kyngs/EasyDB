/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.provider.mysql;

import com.zaxxer.hikari.HikariConfig;

public class MySQLConfig {

    protected final HikariConfig hikariConfig;

    public MySQLConfig() {
        hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    public MySQLConfig(HikariConfig hikariConfig) {
        this.hikariConfig = hikariConfig;
    }

    public HikariConfig getHikariConfig() {
        return hikariConfig;
    }

    public MySQLConfig setJdbcUrl(String url) {
        hikariConfig.setJdbcUrl(url);
        return this;
    }

    public MySQLConfig setUsername(String username) {
        hikariConfig.setUsername(username);
        return this;
    }

    public MySQLConfig setPassword(String password) {
        hikariConfig.setPassword(password);
        return this;
    }

}
