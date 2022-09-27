package org.example.p4.data;


import org.example.p4.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger) throws SQLException;
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findReizigerById(int id) throws SQLException;
    List<Reiziger> findByGbDatum(String datum) throws SQLException;
    List <Reiziger> findAll() throws SQLException;

}
