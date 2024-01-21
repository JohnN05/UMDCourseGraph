package courses.relationship.requisites;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequisiteFactoryTest {

    @Test
    void getRequisite() {
        assertTrue(RequisiteFactory.getRequisite("CMSC132") instanceof CourseReq);
        assertTrue(RequisiteFactory.getRequisite("PERMISSION OF STAT") instanceof DepartmentReq);
        assertTrue(RequisiteFactory.getRequisite("testing") instanceof OtherReq);
    }
}