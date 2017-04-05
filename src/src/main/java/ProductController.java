import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ProductController {

    public static void storeProduct(Product product) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(product);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void deleteProduct(String id) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Product.class, id));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void modifyProduct(String id, Product product) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        Product newProd = entityManager.find(Product.class, id);
        newProd = product;
        entityManager.merge(newProd);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static List<Product> getAllProducts() {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        String query = "SELECT e FROM Product e";
        List<Product> products = entityManager.createQuery(query, Product.class).getResultList();
        entityManager.close();
        return products;
    }

    public static Product getProduct(String id) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        Product product = entityManager.find(Product.class, id);
        entityManager.close();
        return product;
    }
}