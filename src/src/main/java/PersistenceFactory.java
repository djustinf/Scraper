/*  Daniel Justin Foxhoven
    June 2017
    Josh Taylor Senior Project
 */
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*  This class provides a singleton persistence factory. This factory is
    used to access the embedded database and retrieve/store objects.
 */
public class PersistenceFactory {

    private static final PersistenceFactory pf = new PersistenceFactory();

    protected EntityManagerFactory singleton;

    public static PersistenceFactory getInstance() {
        return pf;
    }

    private PersistenceFactory() {}

    /*  Returns an entity manager factory instance using the persistence factory singleton.
     */
    public EntityManagerFactory getEntityManagerFactory() {
        if (singleton == null)
            createEntityManagerFactory();
        return singleton;
    }

    /* Closes the entity manager factory
     */
    public void closeEntityManagerFactory() {
        if (singleton != null) {
            singleton.close();
            singleton = null;
        }
    }

    /* Generates an entity manager factory using the persistence factory
     */
    private void createEntityManagerFactory() {
        singleton = Persistence.createEntityManagerFactory("productDB");
    }
}
