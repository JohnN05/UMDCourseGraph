package courses;

import org.junit.jupiter.api.Test;
import utility.HttpReader;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testEquals() {
        Course temp = new Course("CMSC132", null, -1, null, null, null, null);
        Course course = CourseFactory.processCourse(HttpReader.requestRawCourse("CMSC132"));
        assertEquals(temp, course);
        assertEquals(temp.getCourse_id(), course.getCourse_id());
        assertNotEquals(temp.getName(), course.getName());
        assertNotEquals(temp.getCredits(), course.getCredits());
        assertNotEquals(temp.getDescription(), course.getDescription());
        assertNotEquals(temp.getGen_eds(), course.getGen_eds());
        assertNotEquals(temp.getPrereqs(), course.getPrereqs());
        assertNotEquals(temp.getCoreqs(), course.getCoreqs());
    }
}