import dao.AdresDAOHibernate;
import dao.OVChipkaartDAOHibernate;
import dao.ProductDAOHibernate;
import dao.ReizigerDAOHibernate;
import domain.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) throws SQLException {
//        testFetchAll();
        testDAOHibernate();
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = getSession();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        } finally {
            session.close();
        }
    }

    private static void testDAOHibernate() {
        Session session = getSession();

        ReizigerDAOHibernate rDao = new ReizigerDAOHibernate(session);
        AdresDAOHibernate aDao = new AdresDAOHibernate(session);
        OVChipkaartDAOHibernate ovDao = new OVChipkaartDAOHibernate(session);
        ProductDAOHibernate pDao = new ProductDAOHibernate(session);

        System.out.println("\n---------- Test ReizigerDAOHibernate -------------");
        System.out.println("[Test ReizigerDAOHibernate.findAll() geeft de volgende reizigers:");
        List<Reiziger> reizigers = rDao.findAll();
        for (Reiziger reiziger : reizigers) {
            System.out.println(reiziger);
        }

        System.out.println(String.format("\n[Test ReizigerDAOHibernate.findById() geeft de volgende reiziger:\n%s\n",
                rDao.findById(5)));

        System.out.println("[Test ReizigerDAOHibernate.findByGbdatum(\"2001-03-21\") geeft de volgende reizigers:");
        List<Reiziger> reizigerList = rDao.findByGbdatum("2001-03-21");
        for (Reiziger reiziger : reizigerList) {
            System.out.println(reiziger);
        }

        System.out.println("\n[Test ReizigerDAOHibernate.findByOVChipkaart() geeft de volgende reiziger:");
        Date geldigTotDatum = java.sql.Date.valueOf("2020-03-31");
        Date geboortedatum = java.sql.Date.valueOf("1998-08-11");
        Reiziger hidde = new Reiziger(3, "H", null, "Glansdorp",geboortedatum);
        OVChipkaart chipkaart = new OVChipkaart(68514, geldigTotDatum, 1, 2.50, hidde);
        System.out.println(rDao.findByOVChipkaart(chipkaart));

        System.out.println("\n[Test ReizigerDAOHibernate.save()]");
        System.out.print(String.format("Aantal reizigers: %d, ", rDao.findAll().size()));
        geboortedatum = java.sql.Date.valueOf("2001-03-30");
        Reiziger johan = new Reiziger(6, "CHM", null, "Bui", geboortedatum);
        rDao.save(johan);
        System.out.println("na ReizigerDAOHibernate.save(): " + rDao.findAll().size());

        System.out.println("\n[Test ReizigerDAOHibernate.update()]");
        System.out.println("Gegevens van reiziger met reiziger_id #6:\n" + rDao.findById(6));
        johan.setTussenvoegsel("van de");
        johan.setAchternaam("Buurt");
        rDao.update(johan);
        System.out.println("Na ReizigerDAOHibernate.update() zijn de gegevens:\n" + rDao.findById(6));

        System.out.println("\n[Test ReizigerDAOHibernate.delete()]");
        System.out.print(String.format("Aantal reizigers: %d, ", rDao.findAll().size()));
        rDao.delete(rDao.findById(6));
        System.out.println("na ReizigerDAOHibernate.delete(): " + rDao.findAll().size());


        System.out.println("\n---------- Test AdresDAOHibernate -------------");
        System.out.println("[Test AdresDAOHibernate.findAll() geeft de volgende adressen:");
        List<Adres> adressen = aDao.findAll();
        for (Adres adres : adressen) {
            System.out.println(adres);
        }

        System.out.println("\n[Test AdresDAOHibernate.findByReiziger() geeft de volgende adres:");
        System.out.println(aDao.findByReiziger(rDao.findById(1)));

        System.out.println("\n[Test AdresDAOHibernate.save()]");
        System.out.print(String.format("Aantal adressen: %d, ", aDao.findAll().size()));
        rDao.save(johan);
        Adres adres = new Adres(6, "3404KE", "1", "dortmund", "Dordrecht", johan);
        aDao.save(adres);
        System.out.println("na AdresDAOHibernate.save(): " + aDao.findAll().size());

        System.out.println("\n[Test ReizigerDAOHibernate.update()]");
        System.out.println("Gegevens van adres van reiziger " + rDao.findById(6).getVoorletters() + ":\n"
                + aDao.findByReiziger(rDao.findById(6)));
        adres.setHuisnummer("551");
        aDao.update(adres);
        System.out.println("Na AdresDAOHibernate.update() zijn de gegevens:\n" + aDao.findByReiziger(rDao.findById(6)));

        System.out.println("\n[Test AdresDAOHibernate.delete()]");
        System.out.print(String.format("Aantal adressen: %d, ", aDao.findAll().size()));
        aDao.delete(aDao.findByReiziger(rDao.findById(6)));
        System.out.println("na AdresDAOHibernate.delete(): " + aDao.findAll().size());
        rDao.delete(rDao.findById(6));


        System.out.println("\n---------- Test OVChipkaartDAOHibernate -------------");
        System.out.println("[Test OVChipkaartDAOHibernate.findAll() geeft de volgende ovchipkaarten:");
        List<OVChipkaart> ovChipkaarten = ovDao.findAll();
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }

        System.out.println(String.format("\n[Test OVChipkaartDAOHibernate.findById() geeft de volgende ovchipkaart:\n%s",
                ovDao.findById(35283)));

        geboortedatum = java.sql.Date.valueOf("2002-09-17");
        Reiziger erik = new Reiziger(2, "E", "de", "Jeager", geboortedatum);
        System.out.println(String.format("\n[Test OVChipkaartDAOHibernate.findByReiziger() geeft de volgende ovchipkaarten:"));
        ovChipkaarten = ovDao.findByReiziger(erik);
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }

        Product product3 = new Product(3, "2e klas", "2e klas", 50.60);
        System.out.println(String.format("\n[Test OVChipkaartDAOHibernate.findByProduct() geeft de volgende ovchipkaarten:"));
        ovChipkaarten = ovDao.findByProduct(product3);
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }

        System.out.println("\n[Test OVChipkaartDAOHibernate.save()]");
        System.out.print(String.format("Aantal ovchipkaarten: %d, ", ovDao.findAll().size()));
        OVChipkaart ovErik = new OVChipkaart(11111, java.sql.Date.valueOf("2022-01-01"), 1, 0.00, erik);
        List<Product> producten = new ArrayList<>();
        producten.add(pDao.findById(5));
        producten.add(pDao.findById(6));
        Product firstClass = new Product(7, "1E klas", "1e Klas reizen.", 240.0);
        producten.add(firstClass);
        ovErik.setProducten(producten);
        pDao.save(firstClass);
        ovDao.save(ovErik);
        System.out.println(String.format("na OVChipkaartDAOHibernate.save(): %d", ovDao.findAll().size()));

        System.out.println("\n[Test OVChipkaartDAOHibernate.update()]");
        System.out.println("Gegevens van OVChipkaart met kaart_nummer #11111:\n" + ovDao.findById(11111));
        ovErik.setKlasse(1);
        ovErik.setSaldo(100.00);
        producten.remove(firstClass);
        producten.add(pDao.findById(4));
        ovErik.setProducten(producten);
        ovDao.update(ovErik);
        System.out.println("Na OVChipkaartDAOHibernate.update() zijn de gegevens:\n" + ovDao.findById(11111));

        System.out.println("\n[Test OVChipkaartDAOHibernate.delete()]");
        System.out.print(String.format("Aantal ovchipkaarten: %d, ", ovDao.findAll().size()));
        ovDao.delete(ovDao.findById(11111));
        System.out.println(String.format("na OVChipkaartDAOHibernate.delete(): %d", ovDao.findAll().size()));


        //TEST ProductDAOHibernate
        System.out.println("\n---------- Test ProductDAOHibernate -------------");
        System.out.println("[Test ProductDAOHibernate.findAll() geeft de volgende producten:");
        producten = pDao.findAll();
        for (Product product : producten) {
            System.out.println(product);
        }

        System.out.println(String.format("\n[Test ProductDAOHibernate.findById() geeft de volgende ovchipkaart:\n%s",
                pDao.findById(7)));

        System.out.println(String.format("\n[Test ProductDAOHibernate.findByOVChipkaart() geeft de volgende producten:"));
        producten = pDao.findByOVChipkaart(ovDao.findById(35283));
        for (Product product : producten) {
            System.out.println(product);
        }

        System.out.println("\n[Test ProductDAOHibernate.save()]");
        System.out.print(String.format("Aantal producten: %d, ", pDao.findAll().size()));
        Product product8 = new Product(8, "1 Euro reizen", "Altijd en overal reizen voor 1 euro.", 1000.00);
        pDao.save(product8);
        System.out.println(String.format("na ProductDAOHibernate.save(): %d", pDao.findAll().size()));

        System.out.println("\n[Test ProductDAOHibernate.update()]");
        System.out.println("Gegevens van product met product_nummer #8:\n" + pDao.findById(8));
        product8.setNaam("2 Euro reizen");
        product8.setBeschrijving("Altijd en overal reizen voor 2 euro.");
        System.out.println("Na ProductDAOHibernate.update() zijn de gegevens:\n" + pDao.findById(8));

        System.out.println("\n[Test ProductDAOHibernate.delete()]");
        System.out.print(String.format("Aantal producten: %d, ", pDao.findAll().size()));
        pDao.delete(pDao.findById(7));
        pDao.delete(pDao.findById(8));
        System.out.println(String.format("na ProductDAOHibernate.delete(): %d", pDao.findAll().size()));

        session.close();
    }

}
