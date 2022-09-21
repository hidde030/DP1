package org.example.p5;

import org.example.p3.Adres;

import java.sql.SQLException;

public class ProductDAOsql implements ProductDAO {

    @Override
    public boolean save(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        return false;
    }
}
