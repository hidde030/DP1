package domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Reiziger {

    @SequenceGenerator(
            name = "reiziger_sequence",
            sequenceName = "reiziger_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reiziger_sequence"
    )
    @Id
    @Column(name = "reiziger_id")
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    @OneToOne(mappedBy = "reiziger")
    @JoinColumn(name = "adres_id")
    private Adres adres;
    @Transient
    private List<OvChipkaart> ovChipkaartList;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum, Adres adres) {
        this(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
        this.adres = adres;
    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum, List<OvChipkaart> ovChipkaartList) {
        this(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
        this.ovChipkaartList = ovChipkaartList;
    }

    public Reiziger() {

    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return this.voorletters;
    }

    public String getTussenvoegsel() {
        return this.tussenvoegsel;
    }

    public String getAchternaam() {
        return this.achternaam;
    }

    public Date getGeboortedatum() {
        return this.geboortedatum;
    }

    public String toString() {
        return  String.format("#%s %s. %s %s, %s. Met adres: %s", id, voorletters, tussenvoegsel, achternaam, geboortedatum, adres);
    }

    public List<OvChipkaart> getOvChipkaartList() {
        return this.ovChipkaartList;
    }

    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public void setOvChipkaartList(List<OvChipkaart> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }
}