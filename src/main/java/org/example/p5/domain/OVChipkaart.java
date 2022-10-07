package  org.example.p5.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OVChipkaart {
    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private float saldo;
    private Reiziger reiziger;
    private List<Product> producten = new ArrayList<>();

    public OVChipkaart(Date geldigTot, int klasse, float saldo) {
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, float saldo) {
        this(geldigTot, klasse, saldo);
        this.kaartNummer = kaartNummer;
    }

    public OVChipkaart(Date geldigTot, int klasse, float saldo, Reiziger reiziger) {
        this(geldigTot, klasse, saldo);
        this.reiziger = reiziger;
    }

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, float saldo, Reiziger reiziger) {
        this(kaartNummer, geldigTot, klasse, saldo);
        this.reiziger = reiziger;
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

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
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
    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaartNummer=" + kaartNummer +
                ", geldigTot=" + geldigTot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                "}";
    }
}
