package org.example.p4.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int reiziger_id;
    private String voorletters;
    private String tussenvoegsels;
    private String achternaam;
    private Date geboortedatum;

    private Adres adres;
    private List <OVChipkaart> ovChipkaarts_reiziger = new ArrayList<>();



    public Reiziger() {
    }

    public Reiziger(int reiziger_id, String voorletters, String tussenvoegsels, String achternaam, Date geboortedatum) {
        this.reiziger_id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsels = tussenvoegsels;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public void ovkaartToevoegen(OVChipkaart ovChipkaart){
        ovChipkaarts_reiziger.add(ovChipkaart);
    }

    public void ovkaartVerwijderen(OVChipkaart ovChipkaart){ovChipkaarts_reiziger.remove(ovChipkaart);}



    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsels() {
        return tussenvoegsels;
    }

    public void setTussenvoegsels(String tussenvoegsels) {
        this.tussenvoegsels = tussenvoegsels;
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


    public List<OVChipkaart> getOvChipkaarts_reiziger() {
        return ovChipkaarts_reiziger;
    }

    public void setOvChipkaarts_reiziger(List<OVChipkaart> ovChipkaarts_reiziger) {
        this.ovChipkaarts_reiziger = ovChipkaarts_reiziger;
    }
    @Override
    public String toString() {

        return "Reiziger{" +
                "reiziger_id=" + reiziger_id +
                ", voorletters='" + voorletters + '\'' +
                ", tussenvoegsels='" + tussenvoegsels + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", geboortedatum=" + geboortedatum +
                '}' + adres + ovChipkaarts_reiziger;
    }
}