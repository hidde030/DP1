package org.example.p3;

import org.example.p2.Reiziger;
import org.example.p2.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection connection) {
        this.conn = connection;
    }


    @Override
    public boolean save(Adres adres) {
        try {
            String query = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getReiziger_id());
            boolean result = pst.execute();
            pst.close();
            return result;

        }  catch (Exception e) {
            System.err.println("[Exception] Error" + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String query = "UPDATE adres SET huisnummer = ? WHERE adres_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, adres.getHuisnummer());
            pst.setInt(2, adres.getId());
            boolean result = pst.execute();
            pst.close();
            return result;


        } catch (Exception e) {
            System.err.println("[Exception] Error" + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            if (adres != null) {
                String query = "DELETE FROM adres WHERE adres_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, adres.getId());
                boolean result = pst.execute();
                pst.close();
                return result;
            }

        } catch (Exception e) {
            System.err.println("[Exception] Error. : " + e.getMessage());
        }
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * FROM adres WHERE reiziger_id = " + reiziger.getReiziger_id();
            ResultSet rs = st.executeQuery(query);
            Adres adres = null;
            while (rs.next()) {
                int a_id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                adres = new Adres(a_id, postcode, huisnummer, straat, woonplaats, reiziger);
            }
            rs.close();
            st.close();

            return adres;

        } catch (Exception e) {
            System.err.println("[Exception] Error. : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * FROM adres";
            ResultSet rs = st.executeQuery(query);
            Adres adres = null;
            List<Adres> adressen = new ArrayList<>();
            while (rs.next()) {
                int a_id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                adres = new Adres(a_id, postcode, huisnummer, straat, woonplaats);
                adressen.add(adres);
            }

            rs.close();
            st.close();

            return adressen;

        } catch (Exception e) {
            System.err.println("[Exception] Error. : " + e.getMessage());
        }
        return null;
    }
}