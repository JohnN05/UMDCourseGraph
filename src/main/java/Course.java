import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * Course class is used to represent every UMD course.
 * Stores information on a course such as Prerequisites, Restrictions, etc.
 *
 * @author JohnN05
 */
public class Course implements Comparable<Course>{

    //Keywords used to help interpret course requisites
    private static final HashMap<String, String> KEYWORDS = new HashMap<>();
    private static final String[] SPECIAL_KEYWORDS = {"PERMISSION"};
    private static final String[] CONJUNCTIONS = {"OR", "AND"};

    private static final String DEFAULT_SEARCH_QUERIES = "&_openSectionsOnly=on&instructor=&_facetoface=on&_blended=on&_online=on&courseStartCompare=&courseStartHour=&courseStartMin=&courseStartAM=&courseEndHour=&courseEndMin=&courseEndAM=&teachingCenter=ALL";
    private static final int COURSE_NAME_LENGTH = 7;  //length of Course IDs
    private static final Gson gson = new Gson();
    private static TreeSet<Department> allDepartments = new TreeSet<>();


    private String course_id;
    private String name;
    private int credits;
    private String description;
    private Set<Set<String>> gen_eds;
    private Set<Set<String>> prereqs;
    private Set<Set<String>> corereqs;

    static{
        KEYWORDS.put("EARNED", "EXAM"); //for prerequisite exams
        KEYWORDS.put("HIGH","COURSE");  //for high school prerequisite
    }

    /**
     * Gets all the UMD departments and puts them in a TreeSet
     */
    static{
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest getRequest = null;
        HttpResponse<String> getResponse = null;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.umd.io/v1/courses/departments"))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allDepartments = gson.fromJson(getResponse.body(), new TypeToken<TreeSet<Department>>(){}.getType());
    }

    /**
     * Public constructor for Course which processes the data in a raw course for later use
     * @param raw RawCourse that will be processed
     */
    public Course(RawCourse raw){
        course_id = raw.getCourse_id();
        name = raw.getName();
        credits = raw.getCredits();
        description = raw.getDescription();

        processGenEds(raw.getGen_ed());

    }

    /**
     * Processes and interprets the List of Gen-Eds in a RawCourse.  Gen-Ed Sets larger than 1 means the
     * course provides credits that a student can choose from.
     * Course will store Gen-Ed credits as such.  Every set in gen_eds represents a single Gen-Ed credit
     * that can be earned.  If there is more than a single String in a Set, the student may decide which
     * Gen-Ed they would like to earn for the course.
     *
     * @param rawGenEds the original Set of GenEds acquired by the umd.io API
     */
    private void processGenEds(ArrayList<ArrayList<String>> rawGenEds){
        gen_eds = new HashSet<>();
        HashSet<String> genEdOptions = new HashSet<String>();

        //Handles courses that can provide different gen eds
        if(gen_eds.size() > 1){
            genEdOptions.add(rawGenEds.remove(0).get(0));
            genEdOptions.add(rawGenEds.get(0).remove(0));
        }

        if(!rawGenEds.isEmpty()){
            for(String genEd: rawGenEds.get(0)){
                HashSet<String> genEdSet = new HashSet<>();
                genEdSet.add(genEd);

                gen_eds.add(genEdSet);
            }
        }
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Course o) {
        return this.course_id.compareTo(o.course_id);
    }
}
