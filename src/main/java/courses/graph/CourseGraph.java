package courses.graph;

import courses.Course;
import courses.relationship.requisites.CourseReq;
import courses.relationship.requisites.Requisite;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AdjacencyListGraph;

import java.io.IOException;
import java.util.HashSet;

/**
 * A graph containing all courses provided by the University of Maryland - College Park where edges between
 * nodes represent relationships between courses, departments, etc.
 *
 * @author JohnN05
 */

public class CourseGraph {
    private final Graph graph;

    public CourseGraph(String graphName) {
        graph = new AdjacencyListGraph(graphName);
    }

    /**
     * Adds a course and its corresponding relationships to the graph
     *
     * @param course the course being added
     */
    public void addCourseNode(Course course){
        if(graph.getNode(course.getCourse_id()) == null) {
            graph.addNode(course.getCourse_id());
        }

        Node curNode = graph.getNode(course.getCourse_id());
        curNode.setAttribute("course", course);

        if(course.getCoreqs() != null) {
            addRequisites(course.getCourse_id(), course.getCoreqs(), true);
        }
        if(course.getPrereqs() != null) {
            addRequisites(course.getCourse_id(), course.getPrereqs(), false);
        }
    }

    /**
     * Removes a course node from the graph
     *
     * @param course the course being removed
     */
    public void removeCourseNode(Course course){
        graph.removeNode(course.getCourse_id());
    }

    /**
     * Creates edges for the requisites of a class.
     * When relationships contain courses that aren't in the graph, they are created prior to making the edge.
     *
     * @param courseId Course Id of the current Course
     * @param requisites  Set of Edge Sets which will be added
     * @param directed  Whether the edges are directed
     */
    private void addRequisites(String courseId, HashSet<HashSet<Requisite>> requisites, boolean directed){

        for(HashSet<Requisite> reqOptions: requisites) {

            for (Requisite r : reqOptions) {
                if (r instanceof CourseReq) {
                    Course curCourseReq = new Course(r.toString(), null, -1, null, null, null, null);

                    //Adds courses that aren't contained in CourseList
                    if (graph.getNode(r.toString()) == null) {
                        addCourseNode(curCourseReq);
                    }

                    //Creates the edge for the requisite
                    String edgeId = curCourseReq.getCourse_id() + courseId;

                    if(graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, curCourseReq.getCourse_id(), courseId, directed);
                        Edge curEdge = graph.getEdge(edgeId);
                        curEdge.setAttribute("requisite", r);

                    }
                }
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Getter for a Course in the CourseGraph
     *
     * @param courseId id of the Course being returned
     * @return the corresponding Course in the CourseGraph
     */
    public Course getCourse(String courseId){
        Node courseNode = graph.getNode(courseId);
        return courseNode.getAttribute("course", Course.class);
    }
}
