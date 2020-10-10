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

import cz.kyngs.easymysql.MySQL;
import cz.kyngs.easymysql.MySQLBuilder;

import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException {
        MySQLBuilder builder = new MySQLBuilder();
        builder.setUsername("server");
        builder.setPassword("server");
        MySQL mySQL = builder.build();
        mySQL.sync().schedule(connection -> connection.prepareStatement("CREATE SCHEMA test").execute());

    }

}
