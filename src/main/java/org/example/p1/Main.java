package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost/test";
        //DB NAME = test
        //DB USERNAME = postgres
        //DB PASSWORD = root

        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","root");
        props.setProperty("ssl","true");
        Connection conn = DriverManager.getConnection(url, props);

//        String url = "jdbc:postgresql://localhost/test?user=postgres&password=root&ssl=false";
//        Connection conn = DriverManager.getConnection(url);
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery("SELECT * FROM studenten");
//        while (rs.next())
//        {
//            System.out.println(rs.getString(2));
//        }
//        rs.close();
//        st.close();


        LocalDate localDate = LocalDate.now();
        PreparedStatement st = conn.prepareStatement("INSERT INTO studenten (id,naam) VALUES (4,?)");
        st.setObject(1, localDate);
        st.executeUpdate();
        st.close();
    }
}