import courses.CourseList;
import web_ui.CourseWeb;

import java.io.IOException;

/**
 * Client for CourseWeb
 *
 * @author JohnN05
 */
public class Client {
    public static void main(String[] args) throws IOException {
        CourseList courseList = new CourseList();
        new CourseWeb("main",courseList);
    }
}
