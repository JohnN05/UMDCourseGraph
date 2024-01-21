package utility;

import courses.RawCourse;
import departments.Department;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpReaderTest {

    @Test
    void requestRawCourse() {
        RawCourse course = HttpReader.requestRawCourse("CMSC132");
        assertEquals(course.getCourse_id(), "CMSC132");
        assertEquals(course.getName(), "Object-Oriented Programming II");
        assertEquals(course.getDept_id(), "CMSC");
        assertEquals(course.getCredits(), 4);
        assertEquals(course.getGen_ed(), new ArrayList<ArrayList<String>>());
        assertEquals(course.getCore(), new ArrayList<String>());
        assertNull(course.getRelationships().get("coreqs"));
        assertEquals(course.getRelationships().get("prereqs"), "Minimum grade of C- in CMSC131; or must have earned a score of 5 on the A Java AP exam; or must have earned a satisfactory score on the departmental placement exam.");
        assertEquals(course.getRelationships().get("additional_info"), "And minimum grade of C- in MATH140.");
    }

    @Test
    void requestRawCoursePage() throws IOException {
        List<RawCourse> firstPage = HttpReader.requestRawCoursePage(1);
        assertEquals(firstPage.size(), 100);

        for(RawCourse r: firstPage){
            assertNotNull(r);
            assertNotNull(r.getCourse_id());
            assertNotNull(r.getRelationships());
        }
    }

    @Test
    void requestDepartmentSet() {
        List<Department> departments = HttpReader.requestDepartmentSet();
        assertNotNull(departments);

        for(Department d: departments){
            assertEquals(d.getDept_id().length(), 4);
        }
    }
}