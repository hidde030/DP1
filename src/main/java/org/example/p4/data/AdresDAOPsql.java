package org.example.p4.data;
import org.example.p4.domain.Reiziger;
import org.example.p4.domain.Adres;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;

    }

    public void setRDAO(ReizigerDAOPsql rdao){
        this.rdao = rdao;

    }

    @Override
    public boolean save(Adres adres) {

        try {
            String query = "insert into adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, adres.getAdres_id());
            pt.setString(2, adres.getPostcode());
            pt.setString(3, adres.getHuisnummer());
            pt.setString(4, adres.getStraat());
            pt.setString(5, adres.getWoonplaats());
            pt.setInt(6, adres.getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String query = "update adres set adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? where adres_id = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, adres.getAdres_id());
            pt.setString(2, adres.getPostcode());
            pt.setString(3, adres.getHuisnummer());
            pt.setString(4, adres.getStraat());
            pt.setString(5, adres.getWoonplaats());
            pt.setInt(6, adres.getReiziger_id());

            pt.setInt(7, adres.getAdres_id());

            pt.executeUpdate();
            pt.close();

            return true;

        } catch (SQLException e) {
            return false;
        }

    }


    @Override
    public boolean delete(Adres adres) {
        try {
            String query = "delete from adres where adres_id = ? and postcode = ? and huisnummer = ? and straat = ? and woonplaats = ? and reiziger_id = ? ";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, adres.getAdres_id());
            pt.setString(2, adres.getPostcode());
            pt.setString(3, adres.getHuisnummer());
            pt.setString(4, adres.getStraat());
            pt.setString(5, adres.getWoonplaats());
            pt.setInt(6, adres.getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {

        String query = "select * from adres where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, reiziger.getReiziger_id());
        ResultSet myRs = pt.executeQuery();

        if (myRs.next()) {



            int id = myRs.getInt("adres_id");
            String Pc = myRs.getString("postcode");
            String Hn = myRs.getString("huisnummer");
            String Str = myRs.getString("straat");
            String Wp = myRs.getString("woonplaats");
            Adres adres = new Adres(id, Pc, Hn, Str, Wp, reiziger);


            return adres;
        }
        myRs.close();
        pt.close();

        return null;
    }


    @Override
    public List<Adres> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM adres");


        List<Adres> resultaat = conveteerNaarAdresObject(myRs);


        myStmt.close();
        myRs.close();
        return resultaat;
    }


    //deze klasse heb ik zelf toegevoegd zodat findAll & findByDatum er netter uitzien, anders staat het in de methodes zelf:
    public List<Adres> conveteerNaarAdresObject(ResultSet myRs) throws SQLException {

        List<Adres> adreslijst = new ArrayList<>();

        while (myRs.next()) {


            int id = myRs.getInt("adres_id");
            String Pc = myRs.getString("postcode");
            String Hn = myRs.getString("huisnummer");
            String Str = myRs.getString("straat");
            String Wp = myRs.getString("woonplaats");
            int Rid = myRs.getInt("reiziger_id");

            Adres adres = new Adres(id, Pc, Hn, Str, Wp, rdao.findReizigerById(Rid));

            adreslijst.add(adres);

        }
        myRs.close();
        return adreslijst;

    }
}
