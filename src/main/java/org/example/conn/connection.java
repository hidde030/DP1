package org.example.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class connection {
    public static java.sql.Connection getConnection() {
        String url = "jdbc:postgresql://localhost/ov-chipkaart";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "root");
        props.setProperty("ssl", "false");
        try {
            return DriverManager.getConnection(url, props);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
