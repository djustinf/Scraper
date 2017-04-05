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

    @Column(unique = true)
    private String pageUrl;
    private String siteName;

    public Product() {}

    public void setProduct(String product) {
        this.product = product;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    @Override
    public String toString() {
        return siteName + "-" + product;
    }
}
