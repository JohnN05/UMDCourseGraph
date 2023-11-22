import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores the raw information provided by the umd.io API.
 *
 * @author JohnN05
 */
public class RawCourse {
    private String course_id;
    private String name;
    private String dept_id;
    private int credits;
    private String description;
    private ArrayList<ArrayList<String>> gen_ed;
    private ArrayList<String> core;
    private HashMap<String, String> relationships; //additional information contains valuable information including missing corequisites.

    public String getCourse_id() {
        return course_id;
    }

    public String getName() {
        return name;
    }

    public String getDept_id() {
        return dept_id;
    }

    public int getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<ArrayList<String>> getGen_ed() {
        return gen_ed;
    }

    public ArrayList<String> getCore() {
        return core;
    }

    public HashMap<String, String> getRelationships() {
        return relationships;
    }

    @Override
    public String toString() {
        return "RawCourse{" +
                "course_id='" + course_id + '\'' +
                ", name='" + name + '\'' +
                ", dept_id='" + dept_id + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                ", gen_ed=" + gen_ed +
                ", core=" + core +
                ", relationships=" + relationships +
                '}';
    }
}
