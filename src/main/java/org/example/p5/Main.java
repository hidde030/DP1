
package org.example.p5;

import org.example.p5.domain.*;
import org.example.p5.domain.dao.OVChipkaartDAO;
import org.example.p5.domain.dao.ProductDAO;
import org.example.p5.domain.dao.ReizigerDAO;
import org.example.p5.domain.sql.AdresDAOPsql;
import org.example.p5.domain.sql.OVChipkaartDAOPsql;
import org.example.p5.domain.sql.ProductDAOPsql;
import org.example.p5.domain.sql.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;


public class Main {


    private static Connection connection;


    public static void main(String[] args) throws SQLException {

        connection = getConnection();
        try {
            ReizigerDAOPsql rdao = new ReizigerDAOPsql(getConnection());
            AdresDAOPsql adao = new AdresDAOPsql(getConnection());
            OVChipkaartDAOPsql odao = new OVChipkaartDAOPsql(getConnection());
            ProductDAOPsql pdao = new ProductDAOPsql(getConnection(),odao);

            adao.setRdao(rdao);
            rdao.setAdao(adao);
            rdao.setOdao(odao);
            odao.setRdao(rdao);
            odao.setPdao(pdao);
            testProductDAO(pdao, rdao, odao);


        } catch (Exception err) {
            System.out.println("[ERROR MAIN]" + err.getMessage());
        }
        connection.close();
    }


    private static Connection getConnection() throws SQLException {
        //DB NAME = ov-chipkaart
        //DB USERNAME = postgres
        //DB PASSWORD = root
        String url = "jdbc:postgresql://localhost:5432/ov-chipkaart";
        Connection conn = DriverManager.getConnection(url, "postgres", "root");
        return conn;
    }


    private static void testProductDAO(ProductDAO pdao, ReizigerDAO rdao, OVChipkaartDAO odao) throws SQLException {
        System.out.println("\n---------- TestOVChipkaartDAO -------------");
//        FIND ALL METHOD
        List<Product> products = pdao.findAll();
        System.out.println("[Test] ProductDAO.findAll() Producten:");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println();

        // SAVE METHOD
        System.out.println("[Test] Voor ProductDAO.save() eerst " + pdao.findAll().size() + " producten");
        Product product1 = new Product(10, "Product naam", "Productbeschrijving", 10);
        pdao.save(product1);
        System.out.println("[Test] Na ProductDAO.save() producten" + pdao.findAll().size());
        System.out.println();
//        DELETE METHOD
        System.out.println("[Test] Voor ProductDAO.delete() eerst producten " + pdao.findAll().size());
        pdao.delete(product1);
        System.out.println("[Test] Na ProductDAO.delete() producten:  " + pdao.findAll().size());
        System.out.println();

        System.out.println("[Test] Voor ProductDAO.save() eerst producten :  " + pdao.findAll().size());
        Product product = new Product(13, "Product12", "Productbeschrijving", 110);
        Reiziger reiziger = new Reiziger(100, "H", null, "Hidde", Date.valueOf("2020-02-12"));
        OVChipkaart chipkaart = new OVChipkaart(68514, Date.valueOf("2022-02-12"), 1, 10, reiziger);
        product.addOVChipkaart(chipkaart);
        pdao.save(product);
        System.out.println("[Test] Na ProductDAO.save() " + pdao.findByOVChipkaart(chipkaart));
        System.out.println();

        System.out.println("[Test] ProductDAO.findByOVChipkaart()");
        System.out.println(pdao.findByOVChipkaart(chipkaart));
        System.out.println();

        System.out.println("[Test] Voor ProductDAO.update() :");
        System.out.println(pdao.findAll());
        System.out.println("[Test] Na ProductDAO.update() :");
        chipkaart.setSaldo(100);
        product.setNaam("Appels");
        pdao.update(product);
        System.out.println(pdao.findAll());
        System.out.println();

        // Delete product
        System.out.println("[Test] Voor ProductDAO.delete() eerst " + pdao.findAll().size());
        rdao.delete(reiziger);
        odao.delete(chipkaart);
        pdao.delete(product);
        System.out.println("[Test] Na ProductDAO.delete() " + pdao.findAll().size());
        System.out.println();
    }

}