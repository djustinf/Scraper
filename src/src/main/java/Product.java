import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String product;
    private String brand;

    @ElementCollection
    private Map<String, Double> cost = new HashMap<String, Double>();

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

    public void addCost(String desc, double price) {
        cost.put(desc, price);
    }

    public String getBrand() {
        return brand;
    }

    public Map<String, Double> getCost() {
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
