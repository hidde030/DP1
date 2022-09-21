package org.example.p5;

import org.example.p3.Adres;

import java.sql.SQLException;

public interface ProductDAO {
    public boolean save(Adres adres) throws SQLException;
    public boolean update(Adres adres) throws SQLException;
    public boolean delete(Adres adres) throws SQLException;

}
