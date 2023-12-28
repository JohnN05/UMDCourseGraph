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
    private static final double DEFAULT_ZOOM = 0.08;
    private static final int DEFAULT_NODE_LAYOUT_WEIGHT = 200;
    private static final int DEFAULT_EDGE_LAYOUT_WEIGHT = 50;

    //important configuration for GraphStream
    static{
        System.setProperty("org.graphstream.ui", "javafx");
        System.setProperty("org.graphstream.debug", "true");
    }

    public CourseWeb(String graphName, CourseList cList){
        Graph graph  = new AdjacencyListGraph(graphName);
        graph.setStrict(false);
        graph.setAutoCreate(true);

        graph.setAttribute("ui.stylesheet", stylesheet);

        Viewer viewer = graph.display();
        View view = viewer.getDefaultView();
        view.getCamera().setViewPercent(DEFAULT_ZOOM);

        //imports all courses as nodes
        List<Course> courses = cList.getCourses();
        for(Course c: courses){
            addCourseNode(graph, c);
        }

        for(Course c: courses){

            //Establishes relationships with other courses
            for(HashSet<Requisite> prereqs: c.getPrereqs()){

                for(Requisite r: prereqs){
                    if(r instanceof CourseReq){

                        Course curCourseReq = new Course(r.toString(), null, -1, null, null, null, null);

                        //Adds courses that aren't contained in CourseList
                        if(!courses.contains(curCourseReq)){
                            addCourseNode(graph, curCourseReq);
                        }

                        String edgeId = r + c.getCourse_id();
                        graph.addEdge(edgeId, r.toString(), c.getCourse_id(), true);
                        Edge curEdge = graph.getEdge(edgeId);

                        curEdge.setAttribute("layout.weight", DEFAULT_EDGE_LAYOUT_WEIGHT);
                    }
                }
            }
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
}
