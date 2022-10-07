package org.example.p5.domain.dao;

import org.example.p5.domain.OVChipkaart;
import org.example.p5.domain.Product;
import org.example.p5.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    boolean save(OVChipkaart ovChipkaart);
    boolean update(Reiziger reiziger);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    OVChipkaart findByID(int id);
    List<OVChipkaart> findAll() throws SQLException;

    List<OVChipkaart> findByProduct(Product product);

}
