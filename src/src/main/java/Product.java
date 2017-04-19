import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String product;
    private double cost;
    private String brand;

    @Column(unique = true)
    private String amazonUrl;
    @Column(unique = true)
    private String ebayUrl;

    public Product() {}

    public void setProduct(String product) {
        this.product = product;
    }

    public void setEbayUrl(String pageUrl) {
        this.ebayUrl = pageUrl;
    }

    public void setAmazonUrl(String pageUrl) {
        this.amazonUrl = pageUrl;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getBrand() {
        return brand;
    }

    public double getCost() {
        return cost;
    }

    public String getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public String getEbayUrl() {
        return ebayUrl;
    }

    public String getAmazonUrl() {
        return amazonUrl;
    }

    @Override
    public String toString() {
        return brand + "-" + product;
    }
}
