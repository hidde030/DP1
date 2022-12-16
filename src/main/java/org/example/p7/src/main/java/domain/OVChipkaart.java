package domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name = "kaart_nummer")
    private int kaartNummer;
    @Column(name = "geldig_tot")
    private Date geldigTot;
    private int klasse;
    private double saldo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ov_chipkaart_product",
            joinColumns = {@JoinColumn(name = "kaart_nummer")},
            inverseJoinColumns = {@JoinColumn(name = "product_nummer")})
    private List<Product> producten = new ArrayList<>();

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public OVChipkaart(int i, Date geboortedatum, int i1, double v) {
    }

    public OVChipkaart() {
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public void addProduct(Product product) {
        if (!producten.contains(product)) {
            producten.add(product);
        }
    }

    public void removeProduct(Product product) {
        producten.remove(product);
    }


    public boolean equals(Object andereObject) {
        boolean gelijkeObjecten = false;

        if (andereObject instanceof OVChipkaart) {
            OVChipkaart andereOVChipkaart = (OVChipkaart) andereObject;

            if (this.kaartNummer == andereOVChipkaart.getKaartNummer() &&
                this.geldigTot.equals(andereOVChipkaart.getGeldigTot()) &&
                this.klasse == andereOVChipkaart.getKlasse() &&
                this.saldo == andereOVChipkaart.getSaldo() &&
                this.reiziger.getReizigernummer() == andereOVChipkaart.getReiziger().getReizigernummer() &&
                this.producten.equals(andereOVChipkaart.getProducten())) {
                gelijkeObjecten = true;
            }
        }

        return gelijkeObjecten;
    }

    public String toString() {
        StringBuilder s;
        s = new StringBuilder(String.format("OVChipkaart {#%d, Geldig tot: %s, %de klasse, Saldo: €%.2f, Reiziger: #%d}",
                getKaartNummer(),
                getGeldigTot(),
                getKlasse(),
                getSaldo(),
                getReiziger().getReizigernummer()));

        if (!producten.isEmpty()) {
            s.append(String.format("\nProducten op OVChipkaart #%d:", getKaartNummer()));
            for (Product product : producten) {
                s.append("\n");
                s.append(product);
            }
        }

        s.append("\n");

        return s.toString();
    }
}
