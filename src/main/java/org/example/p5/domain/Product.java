package  org.example.p5.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private float prijs;
    private List<OVChipkaart> ovChipkaartArrayList = new ArrayList<>();

    public Product(int product_nummer, String naam, String beschrijving, float prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product(int product_nummer, String naam, String beschrijving, float prijs, List<OVChipkaart> list){
        this(product_nummer, naam, beschrijving, prijs);
        this.ovChipkaartArrayList = list;
    }

    public Product() {

    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
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

    public float getPrijs() {
        return prijs;
    }

    public void setPrijs(float prijs) {
        this.prijs = prijs;
    }
    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaartArrayList;
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaartArrayList = ovChipkaarten;
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (!ovChipkaartArrayList.contains(ovChipkaart)) {
            ovChipkaartArrayList.add(ovChipkaart);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }
}
