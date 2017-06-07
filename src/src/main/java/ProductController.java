/*  Daniel Justin Foxhoven
    June 2017
    Josh Taylor Senior Project
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/*  This class is responsible for providing a database access layer. It utilizes the
    PersistenceFactory class to access the database.
 */
public class ProductController {

    /*  Method for storing a product in the database.
     */
    public static void storeProduct(Product product) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(product);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*  Method for removing a product from the database
     */
    public static void deleteProduct(String id) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Product.class, id));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /* Method for modifying a database entry.
     */
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

    /*  Returns all products currently in the database
     */
    public static List<Product> getAllProducts() {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        String query = "SELECT e FROM Product e";
        List<Product> products = entityManager.createQuery(query, Product.class).getResultList();
        entityManager.close();
        return products;
    }

    /*  Gets a product from the database based on the product ID
     */
    public static Product getProduct(String id) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        Product product = entityManager.find(Product.class, id);
        entityManager.close();
        return product;
    }
}