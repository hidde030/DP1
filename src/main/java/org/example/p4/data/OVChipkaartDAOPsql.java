package org.example.p4.data;
import org.example.p4.domain.OVChipkaart;
import org.example.p4.domain.Reiziger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;

    private ReizigerDAOPsql rdao;
    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }


    public void setRDAO(ReizigerDAOPsql rdao) {
        this.rdao = rdao;
    }
    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        System.out.println(ovChipkaart);
        try {
            String query = "INSERT INTO ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger().getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public boolean update(OVChipkaart ovChipkaart) {

        try {
            String query = "UPDATE ov_chipkaart set kaart_nummer = ?, geldig_tot = ? , klasse = ? , saldo = ? , reiziger_id = ? where reiziger_id = ? ";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger().getReiziger_id());

            pt.setInt(6, ovChipkaart.getReiziger().getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;


        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        System.out.println(ovChipkaart);
        try {
            String query = "DELETE FROM ov_chipkaart where kaart_nummer=?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());

            pt.executeUpdate();
            pt.close();

            return true;
        } catch (SQLException e) {
            return false;
        }

    }


    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, reiziger.getReiziger_id());
        ResultSet rs = pt.executeQuery();

        List<OVChipkaart> chipkaartList = new ArrayList<>();

        if (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaart_nummer(rs.getInt(1));
            ovChipkaart.setGeldig_tot(Date.valueOf(rs.getString(2)));
            ovChipkaart.setKlasse(rs.getInt(3));
            ovChipkaart.setSaldo(rs.getDouble(4));
            ovChipkaart.setReiziger(reiziger);

            ovChipkaart.setReiziger(reiziger);
            chipkaartList.add(ovChipkaart);

        }

        rs.close();
        pt.close();
        return chipkaartList;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet rs = myStmt.executeQuery("SELECT * FROM ov_chipkaart");


        List<OVChipkaart> res = chipkaartList(rs);


        myStmt.close();
        rs.close();
        return res;
    }

    public List<OVChipkaart> chipkaartList(ResultSet rs) throws SQLException {

        List<OVChipkaart> chipkaartList = new ArrayList<>();

        while (rs.next()) {

            int kaart_nummer = rs.getInt("kaart_nummer");
            Date geldig_tot = rs.getDate("geldig_tot");
            int klasse = rs.getInt("klasse");
            double saldo = rs.getDouble("saldo");
            int reiziger_id = rs.getInt("reiziger_id");

            OVChipkaart ov = new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, rdao.findReizigerById(reiziger_id));

            chipkaartList.add(ov);

        }
        rs.close();
        return chipkaartList;

    }

}
