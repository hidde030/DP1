package org.example.p5.domain.sql;

import org.example.p5.domain.dao.OVChipkaartDAO;
import org.example.p5.domain.dao.ProductDAO;
import org.example.p5.domain.OVChipkaart;
import org.example.p5.domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {

    Connection conn;
    OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql(Connection connection, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = connection;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) {
        int productsSaved = 0;

        String q = "insert into product (product_nummer, naam, beschrijving, prijs) values (?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(q)) {
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            productsSaved = pst.executeUpdate();

            List<OVChipkaart> ovChipkaarten = product.getOvChipkaarten();

            q = "insert into ov_chipkaart_product (kaart_nummer, product_nummer) values (?, ?)";

            for (OVChipkaart ovChipkaart : ovChipkaarten) {
                if (!ovChipkaartDAO.findAll().contains(ovChipkaart)) {
                    ovChipkaartDAO.save(ovChipkaart);
                }
                try (PreparedStatement pst2 = conn.prepareStatement(q)) {
                    pst2.setInt(1, ovChipkaart.getKaartNummer());
                    pst2.setInt(2, product.getProduct_nummer());
                    pst2.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsSaved > 0;
    }

    @Override
    public boolean delete(Product product) {
        int productsDeleted = 0;

        String query = "delete from ov_chipkaart_product where product_nummer = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, product.getProduct_nummer());
            pst.executeUpdate();

            query = "delete from product where product_nummer = ? and " +
                    "naam = ? and " +
                    "beschrijving = ? and " +
                    "prijs = ?";

            try (PreparedStatement pst2 = conn.prepareStatement(query)) {
                pst2.setInt(1, product.getProduct_nummer());
                pst2.setString(2, product.getNaam());
                pst2.setString(3, product.getBeschrijving());
                pst2.setDouble(4, product.getPrijs());
                productsDeleted = pst2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsDeleted > 0;
    }


    @Override
    public boolean update(Product product) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE product " +
                    "SET naam=?, beschrijving=?, prijs=? " +
                    "WHERE product_nummer=?");
            ps.setString(1, product.getNaam());
            ps.setString(2, product.getBeschrijving());
            ps.setFloat(3, product.getPrijs());
            ps.setInt(4, product.getProduct_nummer());
            if (ps.executeUpdate() != 0)
                return true;
        } catch (SQLException throwables) {
        }
        return false;
    }

    @Override
    public Product findByID(int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT product_nummer, naam, beschrijving, prijs " +
                "FROM product " +
                "WHERE product_nummer=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Product product = new Product(
                rs.getInt("product_nummer"),
                rs.getString("naam"),
                rs.getString("beschrijving"),
                rs.getFloat("prijs")
        );
        return product;
    }

    @Override
    public boolean addOV(int kaartnummer, int productnummer) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer, last_update) " +
                    "VALUES (?, ?, ?)");
            ps.setInt(1, kaartnummer);
            ps.setInt(2, productnummer);
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            if (ps.executeUpdate() != 0)
                return true;
        } catch (SQLException throwables) {
        }
        return false;
    }

    @Override
    public boolean removeOV(int kaartnummer, int productnummer) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM ov_chipkaart_product " +
                    "WHERE kaartnummer=? AND productnummer=?;");
            ps.setInt(1, kaartnummer);
            ps.setInt(2, productnummer);
            if (ps.executeUpdate() != 0)
                return true;
        } catch (SQLException e) {
        }
        return false;

    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();

        String q = "select p.product_nummer, p.naam, p.beschrijving, p.prijs " +
                "from product p " +
                "join ov_chipkaart_product ovcp " +
                "on p.product_nummer = ovcp.product_nummer " +
                "where ovcp.kaart_nummer = ?";

        try (PreparedStatement pst = conn.prepareStatement(q)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProduct_nummer(rs.getInt("product_nummer"));
                product.setNaam(rs.getString("naam"));
                product.setBeschrijving(rs.getString("beschrijving"));
                product.setPrijs(rs.getFloat("prijs"));
                producten.add(product);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return producten;
    }

    @Override
    public List<OVChipkaart> findOVCHipkaartenByProduct(Product product) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT kaart_nummer" +
                "FROM ov_chipkaart_product ovp " +
                "WHERE ovp.product_nummer = ?;");
        ps.setInt(1, product.getProduct_nummer());
        ResultSet rs = ps.executeQuery();
        List<OVChipkaart> list = new ArrayList<OVChipkaart>();
        return list;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> producten = new ArrayList<>();

        String q = "SELECT * FROM product";

        try (PreparedStatement pst = conn.prepareStatement(q)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProduct_nummer(rs.getInt("product_nummer"));
                product.setNaam(rs.getString("naam"));
                product.setBeschrijving(rs.getString("beschrijving"));
                product.setPrijs(rs.getFloat("prijs"));
                product.setOvChipkaarten(ovChipkaartDAO.findByProduct(product));
                producten.add(product);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return producten;
    }

}
