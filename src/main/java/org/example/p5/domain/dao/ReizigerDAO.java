package org.example.p5.domain.dao;

import org.example.p5.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {

    public boolean save(Reiziger reiziger);

    public boolean update(Reiziger reiziger);

    public boolean delete(Reiziger reiziger);

    public Reiziger findById(int id);

    public List<Reiziger> findByGbdatum(String date) throws SQLException;

    public List<Reiziger> findAll() throws SQLException;
}
