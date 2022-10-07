package org.example.p5.domain.sql;

import org.example.p5.domain.dao.OVChipkaartDAO;
import org.example.p5.domain.dao.ProductDAO;
import org.example.p5.domain.dao.ReizigerDAO;
import org.example.p5.domain.OVChipkaart;
import org.example.p5.domain.Product;
import org.example.p5.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    Connection conn;
    private ReizigerDAO reizigerDAO;
    private ProductDAO productDAO;
    public OVChipkaartDAOPsql(Connection connection) {
        this.conn = connection;
    }
    public void setRdao(ReizigerDAOPsql rdao) {
        this.reizigerDAO = rdao;
    }
    public void setPdao(ProductDAOPsql pdao) {
        this.productDAO = pdao;
    }
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT kaart_nummer, geldig_tot, klasse, saldo FROM ov_chipkaart " +
                    "WHERE reiziger_id = ?");
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();
            List<OVChipkaart> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getFloat("saldo"),
                        reiziger
                ));
            }
            return list;
        } catch (SQLException throwables) {
            return null;
        }
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES(?, ?, ?, ?, ?);");
            ps.setInt(1, ovChipkaart.getKaartNummer());
            ps.setDate(2, new Date(ovChipkaart.getGeldigTot().getTime()));
            ps.setInt(3, ovChipkaart.getKlasse());
            ps.setFloat(4, ovChipkaart.getSaldo());
            ps.setInt(5, ovChipkaart.getReiziger().getId());
            if (ps.executeUpdate() == 0)
                return update(ovChipkaart);
            return true;

        } catch (SQLException throwables) {
            return update(ovChipkaart);
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList().isEmpty())
                return false;
            List<Integer> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            Iterator iterator = reiziger.getOvChipkaartList().listIterator();
            while (iterator.hasNext()) {
                sb.append(((OVChipkaart) iterator.next()).getKaartNummer());
                if (iterator.hasNext())
                    sb.append(", ");
            }
            PreparedStatement ps = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE reiziger_id = ? AND not kaart_nummer IN (" + sb + ");");
            ps.setInt(1, reiziger.getId());

            ps.executeUpdate();

            reiziger.getOvChipkaartList().forEach((ovchipkaart) -> save(ovchipkaart));
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=?, reiziger_id=? WHERE kaart_nummer=?;");
            ps.setDate(1, new Date(ovChipkaart.getGeldigTot().getTime()));
            ps.setInt(2, ovChipkaart.getKlasse());
            ps.setFloat(3, ovChipkaart.getSaldo());
            ps.setInt(4, ovChipkaart.getReiziger().getId());
            ps.setInt(5, ovChipkaart.getKaartNummer());
            if (ps.executeUpdate() == 0)
                return false;
            return true;

        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE from ov_chipkaart WHERE kaart_nummer=?");
            ps.setInt(1, ovChipkaart.getKaartNummer());
            if (ps.executeUpdate() == 0)
                return false;
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public OVChipkaart findByID(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart " +
                    "WHERE kaart_nummer = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getFloat("saldo"),
                    new ReizigerDAOPsql(conn).findById(rs.getInt("reiziger_id"))
            );
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart ");
            ResultSet rs = ps.executeQuery();
            List<OVChipkaart> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getFloat("saldo"),
                        new ReizigerDAOPsql(conn).findById(rs.getInt("reiziger_id"))
                ));
            }
            return list;
        } catch (SQLException throwables) {
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();

        String q = "select ovc.kaart_nummer, ovc.geldig_tot, ovc.klasse, ovc.saldo, ovc.reiziger_id " +
                "from ov_chipkaart ovc " +
                "join ov_chipkaart_product ovcp " +
                "on ovc.kaart_nummer = ovcp.kaart_nummer " +
                "where ovcp.product_nummer = ?";

        try (PreparedStatement pst = conn.prepareStatement(q)) {
            pst.setInt(1, product.getProduct_nummer());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaartNummer(rs.getInt("kaart_nummer"));
                ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
                ovChipkaart.setKlasse(rs.getInt("klasse"));
                ovChipkaart.setSaldo(rs.getFloat("saldo"));
                ovChipkaart.setReiziger(reizigerDAO.findById(rs.getInt("reiziger_id")));
                ovChipkaart.setProducten(productDAO.findByOVChipkaart(ovChipkaart));
                ovChipkaarten.add(ovChipkaart);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ovChipkaarten;
    }


}
