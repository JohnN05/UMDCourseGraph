package courses;

import utility.HttpReader;

import java.util.List;

/**
 * Provides the client with a list of all UMD courses
 *
 * @author JohnN05
 */
public class CourseList {

    private List<Course> courses;

    public CourseList(){
        courses = HttpReader.requestMinifiedCourses();
    }

    public List<Course> getCourses() {
        return courses;
    }
}
