package courses;

import departments.DepartmentList;
import utility.HttpReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the client with a list of all UMD courses
 *
 * @author JohnN05
 */
public class CourseList {
    private static final int COURSE_ID_LENGTH = 7;
    private final List<Course> courses;

    public CourseList(){
        courses = new ArrayList<>();
        int pageCount = 1;
        List<RawCourse> pageOfCourses = HttpReader.requestRawCoursePage(pageCount);
        while(pageOfCourses.size() == HttpReader.PER_PAGE){
            for(RawCourse r: pageOfCourses){
                System.out.println("Processing: " + r.getCourse_id());
                courses.add(CourseFactory.processCourse(r));
            }

            pageOfCourses = HttpReader.requestRawCoursePage(++pageCount);
        }
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
