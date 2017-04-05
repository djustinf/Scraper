import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class PreferencesController {

    public static void storePreferences(Preferences preferences) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(preferences);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void deletePreferences(String id) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Preferences.class, id));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void modifyPreferences(String id, Preferences preferences) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        entityManager.getTransaction().begin();
        Preferences newPreferences = entityManager.find(Preferences.class, id);
        newPreferences = preferences;
        entityManager.merge(newPreferences);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static List<Preferences> getAllPreferences() {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        String query = "SELECT e FROM Preferences e";
        List<Preferences> preferences = entityManager.createQuery(query, Preferences.class).getResultList();
        entityManager.close();
        return preferences;
    }

    public static Preferences getPreferences(String id) {
        EntityManagerFactory singleton = PersistenceFactory.getInstance().getEntityManagerFactory();
        EntityManager entityManager = singleton.createEntityManager();
        Preferences preferences = entityManager.find(Preferences.class, id);
        entityManager.close();
        return preferences;
    }
}
