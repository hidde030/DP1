package org.example.p2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReizigerDAOPsql implements ReizigerDAO {
    private final Connection conn;


    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }
    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        try {
            String query = "INSERT INTO reiziger values (?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getReiziger_id());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.executeUpdate();
            return true;
        } catch (Exception exception) {
            System.out.print("error message : " +  exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        try {

            String query = "UPDATE reiziger set reiziger_id=?, voorletters=?, tussenvoegsel=?, achternaam=?, geboortedatum=? where reiziger_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getReiziger_id());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.setInt(6, reiziger.getReiziger_id());
            pst.executeUpdate();
            return true;
        } catch (Exception exception) {
            System.out.print("error message : " +  exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        try {
            String query = "DELETE FROM reiziger WHERE reiziger_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getReiziger_id());
            pst.executeUpdate();
            return true;
        }catch (Exception exception){
            System.out.print("error message : " +  exception.getMessage());
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        return null;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String query = "SELECT * FROM reiziger";
        PreparedStatement pst = conn.prepareStatement(query);
        List<Reiziger> reizigers = new ArrayList<>();
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int reiziger_id = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = rs.getString("tussenvoegsel");
            String achternaam = rs.getString("achternaam");
            Date geboortedatum = Date.valueOf(new Date(rs.getDate("geboortedatum").getTime()).toLocalDate());
            Reiziger reiziger = new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
            reizigers.add(reiziger);
        }
        return reizigers;    }
}
