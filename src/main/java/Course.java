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
public class Course implements Comparable<Course>, CourseRelationship{

    //Keywords used to help interpret course requisites
    private static final HashMap<String, String> KEYWORDS = new HashMap<>();
    private static final String[] SPECIAL_KEYWORDS = {"PERMISSION"};
    private static final String[] CONJUNCTIONS = {"OR", "AND"};

    private static final String DEFAULT_SEARCH_QUERIES = "&_openSectionsOnly=on&instructor=&_facetoface=on&_blended=on&_online=on&courseStartCompare=&courseStartHour=&courseStartMin=&courseStartAM=&courseEndHour=&courseEndMin=&courseEndAM=&teachingCenter=ALL";
    private static final int COURSE_NAME_LENGTH = 7;  //length of Course IDs
    private static final Gson gson = new Gson();

    private String course_id;
    private String name;
    private int credits;
    private String description;
    private Set<Set<String>> gen_eds;
    private Set<Set<CourseRelationship>> prereqs;
    private Set<Set<CourseRelationship>> corereqs;

    static{
        KEYWORDS.put("EARNED", "EXAM"); //for prerequisite exams
        KEYWORDS.put("HIGH","COURSE");  //for high school prerequisite
    }

    /**
     * Constructor for Course.  Primarily used to locate a Course to process.
     *
     * @param course_id course_id of this object
     */
    public Course(String course_id){
        this.course_id = course_id;
    }

    /**
     * Processes a RawCourse to obtain the remaining missing information
     *
     * @param raw RawCourse that will be converted into a Course
     */
    public void processCourse(RawCourse raw){
        credits = raw.getCredits();
        description = raw.getDescription();

        processGenEds(raw);

    }

    /**
     * Processes and interprets the List of Gen-Eds in a RawCourse.  Gen-Ed Sets larger than 1 means the
     * course provides credits that a student can choose from.
     * Course will store Gen-Ed credits as such.  Every set in gen_eds represents a single Gen-Ed credit
     * that can be earned.  If there is more than a single String in a Set, the student may decide which
     * Gen-Ed they would like to earn for the course.
     *
     * @param raw the RawCourse containing the Gen Eds that will be processed
     */
    private void processGenEds(RawCourse raw){
        ArrayList<ArrayList<String>> rawGenEds = raw.getGen_ed();
        gen_eds = new HashSet<>();
        HashSet<String> genEdSet;

        //Handles courses that can provide different gen eds
        if(rawGenEds.size() > 1){
            genEdSet = new HashSet<>();
            genEdSet.add(rawGenEds.remove(0).get(0));
            genEdSet.add(rawGenEds.get(0).remove(0));

            gen_eds.add(genEdSet);
        }

        if(!rawGenEds.isEmpty()){
            for(String genEd: rawGenEds.get(0)){
                genEdSet = new HashSet<>();
                genEdSet.add(genEd);

                gen_eds.add(genEdSet);
            }
        }
    }

    /**
     * Processes and interprets the prerequisites stored in the relationships map.  Formats the prerequisites
     * into a set of CourseRelationship sets.  Every set in prereqs represents an individual requirement the
     * student must have prior to taking the class.  If there is more than a single CourseRelationship in a set,
     * Only one of the CourseRelationships listed are needed.
     *
     * @param raw the RawCourse containing the prerequisites that will be processed
     */
    private void processPrereqs(RawCourse raw){

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

    public Set<Set<String>> getGen_eds() {
        return gen_eds;
    }

    public Set<Set<CourseRelationship>> getPrereqs() {
        return prereqs;
    }

    public Set<Set<CourseRelationship>> getCorereqs() {
        return corereqs;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                ", gen_eds=" + gen_eds +
                ", prereqs=" + prereqs +
                ", corereqs=" + corereqs +
                '}';
    }

    @Override
    public int compareTo(Course o) {
        return this.course_id.compareTo(o.course_id);
    }
}
