package org.example.p5.domain.dao;

import org.example.p5.domain.OVChipkaart;
import org.example.p5.domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;

    boolean update(Product product) throws SQLException;
    public boolean delete(Product product);

    Product findByID(int id) throws SQLException;

    boolean addOV(int kaartnummer, int productnummer) throws SQLException;

    boolean removeOV(int kaartnummer, int productnummer) throws SQLException;

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;

    List<OVChipkaart> findOVCHipkaartenByProduct(Product product) throws SQLException;
    List<Product> findAll() throws SQLException;
}
