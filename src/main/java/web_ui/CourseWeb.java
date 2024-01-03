package web_ui;

import courses.Course;
import courses.CourseList;
import courses.relationship.requisites.CourseReq;
import courses.relationship.requisites.Requisite;
import departments.Department;
import departments.DepartmentList;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AdjacencyListGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.util.HashSet;
import java.util.List;

/**
 * A directed graph representation of all UMD courses with a user interface
 *
 * @author JohnN05
 */

public class CourseWeb {
    private static final String stylesheet = "url('file:src/main/java/web_ui/stylesheet.css')";
    private static final double DEFAULT_ZOOM = 0.03;
    private static final int DEFAULT_NODE_LAYOUT_WEIGHT = 250;
    private static final int DEFAULT_EDGE_LAYOUT_WEIGHT = 50;
    private static final double DEFAULT_STABILIZATION_LIMIT = 0.5;

    //important configuration for GraphStream
    static{
        System.setProperty("org.graphstream.ui", "javafx");
        System.setProperty("org.graphstream.debug", "true");
    }

    public CourseWeb(String graphName, CourseList cList){
        Graph graph  = new AdjacencyListGraph(graphName);

        graph.setAttribute("ui.stylesheet", stylesheet);
        graph.setAttribute("layout.stabilization-limit", DEFAULT_STABILIZATION_LIMIT);

        final Viewer viewer = graph.display();
        View view = viewer.getDefaultView();
        view.getCamera().setViewPercent(DEFAULT_ZOOM);

        //imports all courses as nodes
        List<Course> courses = cList.getCourses();
        for(Course c: courses){
            addCourseNode(graph, c);
        }

        for(Course c: courses){

            addRequisites(graph, c.getCourse_id(), c.getPrereqs(), "prereq", true);
            addRequisites(graph, c.getCourse_id(), c.getCoreqs(), "coreq", false);
        }
    }

    /**
     * Adds a node representing a course on the CourseWeb
     *
     * @param graph the graph of the CourseWeb
     * @param course the Course being added
     */
    private void addCourseNode(Graph graph, Course course){
        String courseId = course.getCourse_id();

        graph.addNode(courseId);
        Node courseNode = graph.getNode(courseId);

        courseNode.setAttribute("course", course);
        courseNode.setAttribute("ui.label", courseId);
        courseNode.setAttribute("layout.weight", DEFAULT_NODE_LAYOUT_WEIGHT);

        //Creates unique department colors
        List<Department> departments = DepartmentList.getAllDepartments();
        Department courseDepartment = DepartmentList.matchDepartment(courseId);
        int departmentIndex = departments.indexOf(courseDepartment);
        courseNode.setAttribute("ui.color", (float)departmentIndex/departments.size());
    }

    /**
     * Creates edges for the requisites of a class
     *
     * @param graph Graph which edges are added
     * @param courseId Course Id of the current Course
     * @param reqs  Set of Edge Sets which will be added
     * @param edgeClass  UI class of added edges
     * @param directed  Whether the edges are directed
     */
    private void addRequisites(Graph graph, String courseId, HashSet<HashSet<Requisite>> reqs, String edgeClass, boolean directed){

        for(HashSet<Requisite> reqOptions: reqs) {
            int count = 0;

            for (Requisite r : reqOptions) {
                if (r instanceof CourseReq) {
                    Course curCourseReq = new Course(r.toString(), null, -1, null, null, null, null);

                    //Adds courses that aren't contained in CourseList
                    if (graph.getNode(r.toString()) == null) {
                        addCourseNode(graph, curCourseReq);
                    }

                    //Creates the edge for the requisite
                    String edgeId = curCourseReq.getCourse_id() + courseId;

                    if(graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, curCourseReq.getCourse_id(), courseId, directed);
                        Edge curEdge = graph.getEdge(edgeId);

                        curEdge.setAttribute("ui.class", edgeClass);
                        curEdge.setAttribute("layout.weight", DEFAULT_EDGE_LAYOUT_WEIGHT);

                        //Each requisite option has a unique color
                        curEdge.setAttribute("ui.color", (float)count/ reqOptions.size());
                        count++;
                    }
                }
            }
        }
    }
}
