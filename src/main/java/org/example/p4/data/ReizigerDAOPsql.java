package org.example.p4.data;

import org.example.p4.domain.Reiziger;
import org.example.p4.domain.Adres;
import org.example.p4.domain.OVChipkaart;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;


    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }


    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    public void setAdresDAO(AdresDAO adresDAO) {
        this.adresDAO = adresDAO;
    }

    public void setOVDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
    }


    @Override
    public boolean save(Reiziger reiziger) {

        try {
            String query = "insert into reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values (?, ?, ?, ?, ?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, reiziger.getReiziger_id());
            pt.setString(2, reiziger.getVoorletters());
            pt.setString(3, reiziger.getTussenvoegsels());
            pt.setString(4, reiziger.getAchternaam());
            pt.setDate(5, reiziger.getGeboortedatum());

            pt.executeUpdate();
            pt.close();
            if (reiziger.getAdres() != null) {
                adresDAO.save(reiziger.getAdres());

            }

            if (reiziger.getOvChipkaarts_reiziger() != null) {
                for (OVChipkaart o : reiziger.getOvChipkaarts_reiziger()) {
                    ovChipkaartDAO.save(o);
                }
            }

            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public boolean update(Reiziger reiziger) {

        try {
            String query = "update reiziger set reiziger_id = ?, voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? where reiziger_id = ?";

            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1, reiziger.getReiziger_id());
            pt.setString(2, reiziger.getVoorletters());
            pt.setString(3, reiziger.getTussenvoegsels());
            pt.setString(4, reiziger.getAchternaam());
            pt.setDate(5, reiziger.getGeboortedatum());
            pt.setInt(6, reiziger.getReiziger_id());

            pt.executeUpdate();
            pt.close();

            if (reiziger.getAdres() != null) {
                adresDAO.update(reiziger.getAdres());

            }
            if (reiziger.getOvChipkaarts_reiziger() != null) {
                for (OVChipkaart o : reiziger.getOvChipkaarts_reiziger()) {
                    ovChipkaartDAO.update(o);
                }
            }
            return true;

        } catch (SQLException e) {
            return false;

        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {

        try {
            String query = "DELETE FROM reiziger WHERE reiziger_id = ? AND voorletters = ? AND tussenvoegsel = ? AND achternaam = ? AND geboortedatum = ? ";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, reiziger.getReiziger_id());
            pt.setString(2, reiziger.getVoorletters());
            pt.setString(3, reiziger.getTussenvoegsels());
            pt.setString(4, reiziger.getAchternaam());
            pt.setDate(5, reiziger.getGeboortedatum());


            if (reiziger.getAdres() != null) {
                adresDAO.delete(reiziger.getAdres());

            }
            if (reiziger.getOvChipkaarts_reiziger() != null) {
                for (OVChipkaart o : reiziger.getOvChipkaarts_reiziger()) {
                    ovChipkaartDAO.delete(o);
                }
            }
            pt.executeUpdate();
            pt.close();
            return true;

        } catch (SQLException e) {
            return false;

        }

    }


    @Override
    public Reiziger findReizigerById(int id) throws SQLException {

        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, id);
        ResultSet myRs = pt.executeQuery();


        Reiziger reiziger = new Reiziger();
        while (myRs.next()) {
            reiziger.setReiziger_id(Integer.parseInt(myRs.getString(1)));
            reiziger.setVoorletters(myRs.getString(2));
            reiziger.setAchternaam(myRs.getString(4));
            reiziger.setGeboortedatum(Date.valueOf(myRs.getString(5)));

        }

        Adres reizigers_adres = adresDAO.findByReiziger(reiziger);
        if (reizigers_adres != null) {
            reiziger.setAdres(reizigers_adres);
        }

        List<OVChipkaart> o = ovChipkaartDAO.findByReiziger(reiziger);
        if (o != null) {
            reiziger.setOvChipkaarts_reiziger(o);
        }
        pt.close();
        myRs.close();

        return reiziger;

    }


    @Override
    public List<Reiziger> findByGbDatum(String datum) throws SQLException {
return null;
    }


    @Override
    public List<Reiziger> findAll() throws SQLException {
        String query = "SELECT * FROM reiziger";
        PreparedStatement pst = conn.prepareStatement(query);
        List<Reiziger> reizigerArrayList = new ArrayList<>();
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int reiziger_id = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = rs.getString("tussenvoegsel");
            String achternaam = rs.getString("achternaam");
            Date geboortedatum = Date.valueOf(new Date(rs.getDate("geboortedatum").getTime()).toLocalDate());
            Reiziger reiziger = new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
            reizigerArrayList.add(reiziger);
        }
        return reizigerArrayList;
    }

}


