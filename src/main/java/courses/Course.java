package courses;

import com.google.gson.Gson;
import courses.relationship.CourseRelationship;
import departments.Department;
import departments.DepartmentList;

import java.util.*;

/**
 * courses.Course class is used to represent every UMD course.
 * Stores information on a course such as Prerequisites, Restrictions, etc.
 *
 * @author JohnN05
 */
public class Course implements Comparable<Course>, CourseRelationship {

    //Keywords used to interpret course requisites
    private static final String[] CONJUNCTIONS = {"OR", "AND"};
    private static final HashMap<String, String> KEYWORDS = new HashMap<>();
    static{
        KEYWORDS.put("EARNED", "EXAM"); //for prerequisite exams
        KEYWORDS.put("HIGH","COURSE");  //for high school prerequisite
    }

    private static final String[] RELATIONSHIPS = {"coreqs", "prereqs", "restrictions", "additional_info"};
    private static final String DEFAULT_SEARCH_QUERIES = "&_openSectionsOnly=on&instructor=&_facetoface=on&_blended=on&_online=on&courseStartCompare=&courseStartHour=&courseStartMin=&courseStartAM=&courseEndHour=&courseEndMin=&courseEndAM=&teachingCenter=ALL";
    private static final int COURSE_ID_LENGTH = 7;  //length of courses.Course IDs
    private static final Gson gson = new Gson();

    private String course_id;
    private String name;
    private int credits;
    private String description;
    private Set<Set<String>> gen_eds;
    private Set<Set<CourseRelationship>> prereqs;
    private Set<Set<CourseRelationship>> coreqs;



    /**
     * Constructor for courses.Course.  Primarily used to locate a courses.Course to process.
     *
     * @param course_id course_id of this object
     */
    public Course(String course_id){
        this.course_id = course_id;
    }

    /**
     * Processes a courses.RawCourse to obtain the remaining missing information
     *
     * @param raw courses.RawCourse that will be converted into a courses.Course
     */
    public void processCourse(RawCourse raw){
        credits = raw.getCredits();
        description = raw.getDescription();

        processGenEds(raw);

    }

    /**
     * Processes and interprets the List of Gen-Eds in a courses.RawCourse.  Gen-Ed Sets larger than 1 means the
     * course provides credits that a student can choose from.
     * courses.Course will store Gen-Ed credits as such.  Every set in gen_eds represents a single Gen-Ed credit
     * that can be earned.  If there is more than a single String in a Set, the student may decide which
     * Gen-Ed they would like to earn for the course.
     *
     * @param raw the courses.RawCourse containing the Gen Eds that will be processed
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
     * into a set of courses.relationship.CourseRelationship sets.  Every set in prereqs represents an individual requirement the
     * student must have prior to taking the class.  If there is more than a single courses.relationship.CourseRelationship in a set,
     * Only one of the CourseRelationships listed are needed.
     *
     * @param raw the courses.RawCourse containing the prerequisites that will be processed
     */
    private void processPrereqs(RawCourse raw){
        prereqs = new HashSet<>();
        Map<String, String> curRelations = raw.getRelationships();
        String rawPrereqs = curRelations.get(RELATIONSHIPS[1]);

        if(rawPrereqs == null){
            return;
        }

        //Due to the formatting of umd.io's prereqs, prereqs listed with more than one sentence are put in additional_info
        String morePrereqs = curRelations.get(RELATIONSHIPS[3]);
        if(morePrereqs != null){
            String firstWord = morePrereqs.substring(0, morePrereqs.indexOf(" ")).toUpperCase();

            //Identifies displaced prerequisite info with the use of specific conjunctions
            for(String s: CONJUNCTIONS){
                if(s.matches(firstWord)){
                    rawPrereqs += " " + morePrereqs.substring(0, morePrereqs.indexOf("."));
                }
            }
        }

        rawPrereqs = rawPrereqs.replaceAll("-", " ");
        rawPrereqs = rawPrereqs.replaceAll("\\p{Punct}", "").toUpperCase();
        LinkedList<String> rawPrereqWords = new LinkedList<>(Arrays.asList(rawPrereqs.split(" ")));
        LinkedList<String> prereqWords= new LinkedList<>();
        String curWord;

        //recombines certain prerequisite words
        while(!rawPrereqWords.isEmpty()){
            boolean combinedWords = false;
            curWord = rawPrereqWords.getFirst();
            String merged = "";


            //recombines words that mention department permission.  Example: "Permission of ENGR"
            if(rawPrereqWords.getFirst().contains("PERMISSION")){

                for(int word = 0; word < 2; word++){
                    merged += rawPrereqWords.removeFirst() + " ";
                }
                prereqWords.add(merged.trim());
            }

            //recombines words in the list between a keyword and its value
            if(!combinedWords) {
                for (String key : KEYWORDS.keySet()) {
                    int endPos = rawPrereqWords.indexOf(KEYWORDS.get(key));

                    if (rawPrereqWords.contains(key) && endPos != -1) {
                        combinedWords = true;

                        for (int word = 0; word < endPos; word++) {
                            merged += rawPrereqWords.removeFirst() + " ";
                        }

                        prereqWords.add(merged.trim());
                        break;
                    }
                }
            }
        }

        //Applies a whitelist on prereqWords
        Iterator<String> iter = prereqWords.iterator();
        while(iter.hasNext()){
            boolean keepWord = false;
            curWord = iter.next();

            //Keeps department permission requisites
            if(curWord.contains("PERMISSION")){
                keepWord = true;

            }if(!keepWord){

                //Keeps conjunction words
                for(String conjunction: CONJUNCTIONS){
                    if(curWord.matches(conjunction)){
                        keepWord = true;
                        break;
                    }
                }

            }if(!keepWord){

                //Keeps words containing department ids such as course ids
                for(Department d: DepartmentList.getAllDepartments()){
                    String departmentID = d.getDept_id();
                    if(curWord.length() == COURSE_ID_LENGTH && curWord.substring(0,4).equals(departmentID)){
                        keepWord = true;
                        break;
                    }
                }

            }if(!keepWord){
                iter.remove();
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

    public Set<Set<String>> getGen_eds() {
        return gen_eds;
    }

    public Set<Set<CourseRelationship>> getPrereqs() {
        return prereqs;
    }

    public Set<Set<CourseRelationship>> getCoreqs() {
        return coreqs;
    }

    @Override
    public String toString() {
        return "courses.Course{" +
                "course_id='" + course_id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                ", gen_eds=" + gen_eds +
                ", prereqs=" + prereqs +
                ", corereqs=" + coreqs +
                '}';
    }

    @Override
    public int compareTo(Course o) {
        return this.course_id.compareTo(o.course_id);
    }
}
