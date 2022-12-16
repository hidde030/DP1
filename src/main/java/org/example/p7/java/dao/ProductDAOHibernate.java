package dao;

import domain.*;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.Query;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private Session session;

    public ProductDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Product product) {
        boolean productSaved = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.save(product);

            session.getTransaction().commit();
            productSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productSaved;
    }

    @Override
    public boolean update(Product product) {
        boolean productUpdated = false;

        try {
            if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                session.beginTransaction();
            }
            session.update(product);
            session.getTransaction().commit();
            productUpdated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productUpdated;
    }

    @Override
    public boolean delete(Product product) {
        boolean productDeleted = false;

            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.delete(product);
                session.getTransaction().commit();
                productDeleted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        return productDeleted;
    }

    @Override
    public Product findById(int id) {
        return session.get(Product.class, id);
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        Query query = session.createQuery("from Product p join fetch p.ovChipkaarten ovcp where ovcp.kaartNummer = :kaart_nummer");
        query.setParameter("kaart_nummer", ovChipkaart.getKaartNummer());

        return query.getResultList();
    }

    @Override
    public List<Product> findAll() {
        Query query = session.createQuery("from Product");

        return query.getResultList();
    }
}
