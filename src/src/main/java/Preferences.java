import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;

@Entity
public class Preferences {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(unique = true)
    private String name;

    @ElementCollection
    private HashMap<String, String> dataToGather = new HashMap<String, String>();

    public Preferences() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return name;
    }

    public void addTag(String key, String tag) {
        dataToGather.put(key, tag);
    }

    public void removeTag(String key) {
        dataToGather.remove(key);
    }

    public HashMap<String, String> getDataToGather() {
        return dataToGather;
    }
}