package dao;

import domain.*;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.Query;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private Session session;

    public OVChipkaartDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        boolean ovChipkaartSaved = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.save(ovChipkaart);
            session.getTransaction().commit();
            ovChipkaartSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ovChipkaartSaved;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        boolean ovChipkaartSaved = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.update(ovChipkaart);
            session.getTransaction().commit();
            ovChipkaartSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
    }

        return ovChipkaartSaved;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        boolean ovChipkaartDeleted = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.delete(ovChipkaart);
            session.getTransaction().commit();
            ovChipkaartDeleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ovChipkaartDeleted;
    }

    @Override
    public OVChipkaart findById(int id) {
        return session.get(OVChipkaart.class, id);
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        Query query = session.createQuery("from ov_chipkaart where reiziger = :reiziger");
        query.setParameter("reiziger", reiziger);

        return query.getResultList();
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        Query query = session.createQuery("from ov_chipkaart ovc join fetch ovc.producten ovcp where ovcp.productnummer = :product_nummer");
        query.setParameter("product_nummer", product.getProductnummer());

        return query.getResultList();
    }

    @Override
    public List<OVChipkaart> findAll() {
        Query query = session.createQuery("from ov_chipkaart");

        return query.getResultList();
    }
}
