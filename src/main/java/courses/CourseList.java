package courses;

import departments.DepartmentList;
import utility.HttpReader;
import utility.ObjectLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the client with a list of all UMD courses
 *
 * @author JohnN05
 */
public class CourseList implements Serializable {
    private static final String OFFLINE_LIST_PATH = "offline_course_list";
    private static final int COURSE_ID_LENGTH = 7;
    private List<Course> courses;

    public CourseList(){
        List<Course> temp = null;
        try {
            temp = new ArrayList<>();
            int pageCount = 1;
            List<RawCourse> pageOfCourses = HttpReader.requestRawCoursePage(pageCount);
            while (pageOfCourses.size() == HttpReader.PER_PAGE) {
                for (RawCourse r : pageOfCourses) {
                    System.out.println("Processing " + r.getCourse_id());
                    temp.add(CourseFactory.processCourse(r));
                }

                pageOfCourses = HttpReader.requestRawCoursePage(++pageCount);
            }

        //Allows offline access for CourseList when internet is unavailable
        }catch(RuntimeException e){
            loadCourseListFile(OFFLINE_LIST_PATH);
        }

        courses = temp;
        ObjectLoader.save(this, OFFLINE_LIST_PATH);
    }

    /**
     * Alternative constructor which allows a CourseList to be loaded in
     *
     * @param filePath filePath of serialized CourseList
     */
    public CourseList(String filePath){
        loadCourseListFile(filePath);
    }

    /**
     * Loads a CourseList from a file
     *
     * @param filePath file path of the CourseList being loaded
     */
    private void loadCourseListFile(String filePath){
        Object temp = ObjectLoader.load(filePath);

        if(temp instanceof CourseList tempList){
            courses =  tempList.getCourses();
        }else{
            throw new ClassCastException("File isn't a CourseList.");
        }
    }

    /**
     * Removes course variants in the CourseList (i.e. COMM107C)
     */
    public void removeVariants(){
        courses.removeIf(course -> course.getCourse_id().length() > 7);
    }

    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Checks if the text fits the description of a Course ID
     *
     * @param text the text being checked
     * @return true if first four characters are a Department while the last three characters are numbers.
     * Otherwise, returns false.
     */
    public static boolean isCourseID(String text){

        if(text.length() != COURSE_ID_LENGTH || DepartmentList.matchDepartment(text.substring(0, 4)) == null){
            return false;
        }else {

            try {
                Integer.parseInt(text.substring(4));
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
