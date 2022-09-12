package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost/ov-chipkaart";
        //DB NAME = ov-chipkaart
        //DB USERNAME = postgres
        //DB PASSWORD = root
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","root");
//        props.setProperty("ssl","false");
        Connection conn = DriverManager.getConnection(url, props);

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM reiziger");
        System.out.println("Alle reizigers: ");
        while (rs.next())
        {
            System.out.println("#" + rs.getString(1) + ". " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
        }
        rs.close();
        st.close();

    }
}