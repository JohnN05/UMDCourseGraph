import courses.graph.CourseGraph;
import courses.graph.CourseGraphFactory;

import java.io.IOException;

/**
 * Client for CourseWeb
 *
 * @author JohnN05
 */
public class Client {
    public static void main(String[] args) throws IOException {
        CourseGraph courseGraph = CourseGraphFactory.loadCourseGraph("client");
    }
}
