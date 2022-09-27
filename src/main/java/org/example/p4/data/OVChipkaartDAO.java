package org.example.p4.data;


import org.example.p4.domain.OVChipkaart;
import org.example.p4.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;


public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart) throws SQLException;

    boolean update(OVChipkaart ovChipkaart);

    boolean delete(OVChipkaart ovChipkaart) throws SQLException;

    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;

    List<OVChipkaart> findAll() throws SQLException;
}
