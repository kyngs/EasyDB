/*
 * Copyright (c) 2022 kyngs
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
import java.sql.SQLTransientConnectionException;

public class MySQL extends AbstractProvider<Connection, SQLException> {

    private final MySQLConfig config;
    private HikariDataSource dataSource;

    public MySQL(MySQLConfig config) {
        this.config = config;
    }

    @Override
    public void start(EasyDB<?, ?, ?> easyDB) {
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
    public <V> V runTask(ThrowableFunction<Connection, V, SQLException> task) throws SQLException {
        super.runTask(task);

        var connection = dataSource.getConnection();
        var v = task.run(connection);
        connection.close();

        return v;
    }

    @Override
    public boolean identify(Exception e) {
        return e instanceof SQLTransientConnectionException;
    }

}
