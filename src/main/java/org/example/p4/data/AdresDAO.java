package org.example.p4.data;

import org.example.p4.domain.Adres;
import org.example.p4.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save (Adres adres) throws SQLException;
    boolean update (Adres adres);
    boolean delete (Adres adres) throws SQLException;
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    List <Adres> findAll() throws SQLException;

}
