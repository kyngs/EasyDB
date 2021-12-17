/*
 * Copyright (c) 2021 kyngs
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
        var db = new EasyDB(
                new EasyDBConfig()
                        .addProvider(
                                new MySQL(
                                        new MySQLConfig()
                                                .setUsername("test")
                                                .setPassword("test")
                                                .setJdbcUrl("jdbc:mysql://localhost:3306/")
                                )
                        )
        );
        db.runTaskSync(MySQL.class, connection -> {
            var ps = connection.prepareStatement("CREATE SCHEMA `TEST`");
            ps.execute();
        });

        db.runTaskSync(MySQL.class, connection -> {
            var ps = connection.prepareStatement("CREATE TABLE `TEST`.`table` (id INT NOT NULL)");
            ps.execute();
        });
    }

}
