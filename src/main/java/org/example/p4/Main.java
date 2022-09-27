package org.example.p4;

import org.example.p4.data.ReizigerDAOPsql;
import org.example.p4.data.*;
import org.example.p4.domain.*;

import java.sql.*;


public class Main {


    private static Connection connection;


    public static void main(String[] args) throws SQLException {

        connection = getConnection();

        AdresDAOPsql adao = new AdresDAOPsql(getConnection());
        OVChipkaartDAOPsql odao = new OVChipkaartDAOPsql(getConnection());
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(getConnection());
        adao.setRDAO(rdao);
        odao.setRDAO(rdao);
        rdao.setAdresDAO(adao);
        rdao.setOVDAO(odao);


        testOVDAO(odao);
    }


    private static Connection getConnection() throws SQLException {
        //DB NAME = ov-chipkaart
        //DB USERNAME = postgres
        //DB PASSWORD = root
        String url = "jdbc:postgresql://localhost:5432/ov-chipkaart";
        Connection conn = DriverManager.getConnection(url, "postgres", "root");
        return conn;
    }


    private static void testOVDAO(OVChipkaartDAOPsql odao) throws SQLException {
//        Input
        System.out.println("\n---------- Test OVDAO -------------");
        System.out.println("[Test] OVChipkaartDAOPsql.findAll() geeft de volgende OV kaarten:");
        for (OVChipkaart ov : odao.findAll()) {
            System.out.println(ov);

        }
        System.out.println();
        Reiziger reiziger = new Reiziger(91, "S", "", "gla", java.sql.Date.valueOf("2020-03-14"));
        OVChipkaart ovChipkaart = new OVChipkaart(88888, Date.valueOf("2020-02-12"), 1, 22.0, reiziger);
        odao.save(ovChipkaart);

        System.out.println("[Test] OVChipkaartDAOPsql.save() Geeft 1 meer");
        for (OVChipkaart ov : odao.findAll()) {
            System.out.println(ov);
        }
        System.out.println();

        System.out.println("[Test] OVChipkaartDAOPsql.findByReiziger");

        System.out.println(odao.findByReiziger(reiziger));
        System.out.println();
        System.out.println("[Test] OVChipkaartDAOPsql.delete() verwijdert 1 ovchip");
        odao.delete(ovChipkaart);
        for (OVChipkaart ov : odao.findAll()) {
            System.out.println(ov);
        }

    }

}