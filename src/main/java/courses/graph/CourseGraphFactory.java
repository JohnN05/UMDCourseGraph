package courses.graph;

import courses.CourseFactory;
import courses.RawCourse;
import utility.HttpReader;

import java.io.IOException;
import java.util.List;

/**
 * Factory class for CourseGraph
 *
 * @author JohnN05
 */

public class CourseGraphFactory {

    /**
     * Puts all the courses and course relationships provided by UMD in the CourseGraph
     *
     * @param graphName name of the graph
     * @return CourseGraph containing courses and the relationships between them
     * @throws IOException when API is unavailable
     */
    public static CourseGraph loadCourseGraph(String graphName) throws IOException {

        CourseGraph courseGraph = new CourseGraph(graphName);

        //gets every page of the UMD Course directory
        int pageCount = 1;
        List<RawCourse> courseDirectoryPage = HttpReader.requestRawCoursePage(pageCount);

        while (courseDirectoryPage.size() == HttpReader.PER_PAGE) {
            for (RawCourse r : courseDirectoryPage) {
                System.out.println("Processing " + r.getCourse_id());
                courseGraph.addCourseNode(CourseFactory.processCourse(r));
            }

            courseDirectoryPage = HttpReader.requestRawCoursePage(++pageCount);
        }

        return courseGraph;
    }
}
