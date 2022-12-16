package dao;

import domain.*;

import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findById(int id);
    List<Reiziger> findByGbdatum(String datum);
    Reiziger findByOVChipkaart(OVChipkaart ovChipkaart);
    List<Reiziger> findAll();
}