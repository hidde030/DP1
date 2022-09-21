package org.example.p3;

import org.example.p2.Reiziger;
import org.example.p2.ReizigerDAO;
import org.example.p2.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        connection = getConnection();
        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        AdresDAO pdao = new AdresDAOPsql(connection);

        testAdresDAO(pdao, rdao);
        closeConnection();

    }

    private static Connection getConnection() {
        String url = "jdbc:postgresql://localhost/ov-chipkaart?user=postgres&password=root";
        try {
            return connection = DriverManager.getConnection(url);
        } catch (Exception err) {
            System.err.println("ERROR :" + err.getMessage());
        }
        return null;
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (Exception err) {
            System.err.println("ERROR :" + err.getMessage());
        }
    }


    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDao -------------");
        Reiziger reiziger = new Reiziger(100, "M", null, "Glansdorp", java.sql.Date.valueOf("2001-03-30"));
        Adres adres = new Adres(9, "3404KE", "15", "Eemstraat", "IJsselstein", reiziger);
        System.out.print("[Test] DELETE : Reiziger en zijn adres verwijderen indien deze bestaan. \n\n");
        adao.delete(adres);
        rdao.delete(reiziger);
        rdao.save(reiziger);
        System.out.print("[Test] CREATE : Na SQL.AdresDAO.save() heeft deze een adres.\n\n");
        adao.save(adres);
        adres.setReiziger(reiziger);
        reiziger.setAdres(adres);
        System.out.println("Met adres : " + adao.findByReiziger(reiziger));
        System.out.print("[Test] UPDATE : reiziger's huisnummer veranderend  '10'. \n\n");
        adres.setHuisnummer("10");
        adao.update(adres);
        System.out.println(adao.findByReiziger(reiziger));
        System.out.println("[TEST] Findall :  SQL.AdresDAO findAll(). \n");
        List<Adres> adressen = adao.findAll();
        for (Adres adr : adressen) {
//            TOSTRING
            System.out.println(adr.toString());
        }
    }


}
