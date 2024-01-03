import courses.CourseList;
import web_ui.CourseWeb;

/**
 * Client for CourseWeb
 *
 * @author JohnN05
 */
public class Client {
    public static void main(String[] args) {
        CourseList courseList = new CourseList();
        new CourseWeb("main",courseList);
    }
}
