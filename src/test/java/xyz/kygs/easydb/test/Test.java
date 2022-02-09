/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kygs.easydb.test;

import xyz.kyngs.easydb.EasyDB;
import xyz.kyngs.easydb.EasyDBConfig;
import xyz.kyngs.easydb.provider.mysql.MySQL;
import xyz.kyngs.easydb.provider.mysql.MySQLConfig;

public class Test {

    public static void main(String[] args) {
        // Create new EasyDB instance
        var db = new EasyDB<>(
                new EasyDBConfig<>(
                        new MySQL(
                                new MySQLConfig()
                                        .setUsername("test")
                                        .setPassword("test")
                                        .setJdbcUrl("jdbc:mysql://localhost:3306/test")
                        )
                )
                        .setConnectionExceptionHandler(e -> {
                            System.err.println("CONNECTION ERROR!");
                            return true;
                        })
        );

        db.runTaskSync(connection -> {
            var ps = connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS `test`");
            ps.execute();
        });

        db.runTaskSync(connection -> {
            var ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `test`.`table` (id INT NOT NULL)");
            ps.execute();
        });
    }

}
