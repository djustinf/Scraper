/*  Daniel Justin Foxhoven
    June 2017
    Josh Taylor Senior Project
 */
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/*  This class is the main data persisted in the database. This is done using Hibernate.
 */
@Entity
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String product;
    private String brand;
    private double cost;

    @Transient
    private Map<String, String> price = new HashMap<String, String>();

    @Column(length=500, unique = true)
    private String amazonUrl;
    @Column(length=500, unique = true)
    private String ebayUrl;

    public Product() {}

    /*  This class simply contains all of the getters and setters necessary to manipulate the data
        contained within the class.
     */

    public void setCost(double cost) {
        this.cost = cost;
    }

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

    public void addPrice(String desc, String price) {
        this.price.put(desc, price);
    }

    public String getBrand() {
        return brand;
    }

    public Map<String,String> getPrice() {
        return price;
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

    public double getCost() {
        return cost;
    }

    /*  This method is used to generate a text representation of each object.
     */
    @Override
    public String toString() {
        return brand + "-" + product;
    }
}
