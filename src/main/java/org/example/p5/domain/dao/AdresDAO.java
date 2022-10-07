package org.example.p5.domain.dao;

import  org.example.p5.domain.Adres;
import org.example.p5.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findById(int id);
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    List<Adres> findAll() throws SQLException;
}
