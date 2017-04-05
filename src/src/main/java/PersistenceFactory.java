import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceFactory {

    private static final PersistenceFactory pf = new PersistenceFactory();

    protected EntityManagerFactory singleton;

    public static PersistenceFactory getInstance() {
        return pf;
    }

    private PersistenceFactory() {}

    public EntityManagerFactory getEntityManagerFactory() {
        if (singleton == null)
            createEntityManagerFactory();
        return singleton;
    }

    public void closeEntityManagerFactory() {
        if (singleton != null) {
            singleton.close();
            singleton = null;
        }
    }

    private void createEntityManagerFactory() {
        singleton = Persistence.createEntityManagerFactory("schedPU");
    }
}
