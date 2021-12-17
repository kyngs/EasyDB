/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.provider.mysql;

import com.zaxxer.hikari.HikariDataSource;
import xyz.kyngs.easydb.EasyDB;
import xyz.kyngs.easydb.provider.AbstractProvider;
import xyz.kyngs.easydb.scheduler.ThrowableFunction;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL extends AbstractProvider<Connection, SQLException> {

    private final MySQLConfig config;
    private HikariDataSource dataSource;

    public MySQL(MySQLConfig config) {
        this.config = config;
    }

    @Override
    public void start(EasyDB easyDB) {
        dataSource = new HikariDataSource(config.hikariConfig);
    }

    @Override
    public void open() {
        super.open();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void stop() {
        dataSource.close();
    }

    @Override
    public <V> V runTask(ThrowableFunction<Connection, SQLException> task) {
        super.runTask(task);
        V v;
        try {
            var connection = dataSource.getConnection();
            v = task.run(connection);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return v;
    }

}
