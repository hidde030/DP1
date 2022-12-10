package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    @Id
    @Column(name = "product_nummer")
    private int id;
    private String naam;
    private String beschrijving;
    private Double prijs;
    @ManyToMany
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "product_nummer"),
            inverseJoinColumns = @JoinColumn(name = "kaart_nummer")
    )
    private List<OvChipkaart> ovChipkaartList = new ArrayList<>();

    public Product(int id, String naam, String beschrijving, Double prijs) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product(int id, String naam, String beschrijving, Double prijs, List<OvChipkaart> ovChipkaartList) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovChipkaartList = ovChipkaartList;
    }

    public Product() {
    }

    public List<OvChipkaart> getOvchipkaartList() {
        return ovChipkaartList;
    }

    public void setOvChipkaartList(List<OvChipkaart> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }

    public void addChipkaart(OvChipkaart ovChipkaart) {
        if (ovChipkaartList == null) {
            this.ovChipkaartList = new ArrayList<>();
        }
        this.ovChipkaartList.add(ovChipkaart);
    }

    public void removeChipkaart(OvChipkaart ovChipkaart) {
        this.ovChipkaartList.remove(ovChipkaart);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }

    @Override
    public String toString() {;
        return String.format("Product {%s, %s, %s, €%s}", id, naam, beschrijving, prijs);
    }
}