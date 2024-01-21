package courses.graph;

import courses.Course;
import courses.CourseFactory;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.jupiter.api.Test;
import utility.HttpReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CourseGraphTest {

    @Test
    void addCourseNode() throws IOException {
        CourseGraph tempCourseGraph = new CourseGraph("test");
        Graph tempGraph = tempCourseGraph.getGraph();
        Course temp = new Course("CMSC132", null, -1, null, null, null, null);
        Course course = CourseFactory.processCourse(HttpReader.requestRawCourse("CMSC132"));

        tempCourseGraph.addCourseNode(temp);
        Node tempNode = tempGraph.getNode("CMSC132");

        assertNotNull(tempNode);
        assertEquals(tempNode.getAttribute("course"), temp);

        tempCourseGraph.addCourseNode(course);
        tempNode = tempGraph.getNode("CMSC132");
        assertNotNull(tempNode);
        assertEquals(tempNode.getAttribute("course", Course.class), course);

    }

    @Test
    void addRequisites() throws IOException {
        CourseGraph tempCourseGraph = new CourseGraph("test");
        Graph tempGraph = tempCourseGraph.getGraph();
        Course course = CourseFactory.processCourse(HttpReader.requestRawCourse("CMSC132"));
        tempCourseGraph.addCourseNode(course);

        assertNotNull(tempGraph.getNode("MATH140"));
        assertNotNull(tempGraph.getNode("CMSC131"));
        assertEquals(tempGraph.getNodeCount(), 3);
    }

    @Test
    void getCourse() throws IOException {
        CourseGraph tempCourseGraph = new CourseGraph("test");
        Course course = CourseFactory.processCourse(HttpReader.requestRawCourse("CMSC132"));
        tempCourseGraph.addCourseNode(course);
        assertEquals(tempCourseGraph.getCourse(course.getCourse_id()), course);
    }
}