package web_ui;

import courses.Course;
import courses.CourseList;
import courses.relationship.requisites.CourseReq;
import courses.relationship.requisites.Requisite;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AdjacencyListGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.util.HashSet;

/**
 * A directed graph representation of all UMD courses with a user interface
 *
 * @author JohnN05
 */

public class CourseWeb {
    private static final String stylesheet = "url('file:src/main/java/web_ui/stylesheet.css')";

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
        view.getCamera().setViewPercent(0.25);

        //imports all courses as nodes
        for(Course c: cList.getCourses()){
            String curCourseId = c.getCourse_id();

            graph.addNode(curCourseId);
            Node courseNode = graph.getNode(curCourseId);
            courseNode.setAttribute("ui.label", curCourseId);
        }

        for(Course c: cList.getCourses()){
            for(HashSet<Requisite> requisites: c.getPrereqs()){
                for(Requisite r: requisites){
                    if(r instanceof CourseReq){
                        graph.addEdge(r + c.getCourse_id(), r.toString(), c.getCourse_id(), true);
                    }
                }
            }
        }
    }
}
