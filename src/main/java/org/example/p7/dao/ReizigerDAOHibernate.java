package dao;

import domain.*;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.Query;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private Session session;

    public ReizigerDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        boolean reizigerSaved = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.save(reiziger);

            session.getTransaction().commit();
            reizigerSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reizigerSaved;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        boolean reizigerUpdated = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.update(reiziger);

            session.getTransaction().commit();
            reizigerUpdated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reizigerUpdated;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        boolean reizigerDeleted = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }

            session.delete(reiziger);
            session.getTransaction().commit();
            reizigerDeleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reizigerDeleted;
    }

    @Override
    public Reiziger findById(int id) {
        return session.get(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        Query query = session.createQuery("from Reiziger where geboortedatum = :geboortedatum");
        query.setParameter("geboortedatum", java.sql.Date.valueOf(datum));

        return query.getResultList();
    }

    @Override
    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart) {
        int reiziger_id = ovChipkaart.getReiziger().getReizigernummer();

        return findById(reiziger_id);
    }

    @Override
    public List<Reiziger> findAll() {
        Query query = session.createQuery("from Reiziger");

        return query.getResultList();
    }
}
