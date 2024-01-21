package courses;

import courses.relationship.requisites.CourseReq;
import courses.relationship.requisites.Requisite;
import courses.relationship.requisites.RequisiteFactory;
import org.junit.jupiter.api.Test;
import utility.HttpReader;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseFactoryTest {

    @Test
    void processCourse() {
        Course course = CourseFactory.processCourse(HttpReader.requestRawCourse("CMSC132"));
        assertEquals(course.getCourse_id(), "CMSC132");
        assertEquals(course.getName(), "Object-Oriented Programming II");
        assertEquals(course.getCredits(), 4);
        assertEquals(course.getDescription(), "Introduction to use of computers to solve problems using software engineering principles. Design, build, test, and debug medium -size software systems and learn to use relevant tools. Use object-oriented methods to create effective and efficient problem solutions. Use and implement application programming interfaces APIs. Programming done in Java.");

        HashSet<HashSet<Requisite>> prereqs = new HashSet<>();
        HashSet<Requisite> temp = new HashSet<>();
        temp.add(new CourseReq("CMSC131"));
        temp.add(RequisiteFactory.getRequisite("earned a score of 5 on the A Java AP exam".toUpperCase()));
        temp.add(RequisiteFactory.getRequisite("earned a satisfactory score on the departmental placement exam".toUpperCase()));
        prereqs.add(temp);

        temp.clear();
        temp.add(new CourseReq("MATH140"));
        prereqs.add(temp);

        assertEquals(course.getPrereqs(), prereqs);

        HashSet<HashSet<Requisite>> coreqs = new HashSet<>();
        assertEquals(course.getCoreqs(), coreqs);
    }
}