package domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
public class Reiziger {
    @Id
    @Column(name = "reiziger_id")
    private int reizigernummer;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;

    @OneToOne(mappedBy = "reiziger", cascade = CascadeType.ALL)
    private Adres adres;
    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL)
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();

    public Reiziger(int reizigernummer, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reizigernummer = reizigernummer;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger() {
    }

    public int getReizigernummer() {
        return reizigernummer;
    }

    public void setReizigernummer(int reizigernummer) {
        this.reizigernummer = reizigernummer;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOVChipkaarten() {
        return ovChipkaarten;
    }

    public void setOVChipkaarten(List<OVChipkaart> OVChipkaarten) {
        this.ovChipkaarten = OVChipkaarten;
    }

    public String toString() {
        String s = String.format("Reiziger {#%d %s. ",
                getReizigernummer(),
                getVoorletters());

        if (getTussenvoegsel() != null) {
            s += getTussenvoegsel() + " ";
        }

        s += getAchternaam() + " (" + getGeboortedatum() + ")";

        if (adres != null) {
            s += adres.toString();
        }

        s += "}";

        return s;
    }
}
