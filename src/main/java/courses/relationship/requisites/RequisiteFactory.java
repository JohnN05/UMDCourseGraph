package courses.relationship.requisites;

import courses.CourseList;
import departments.Department;
import departments.DepartmentList;

/**
 * Factory for all the Requisites
 *
 * @author JohnN05
 */

public class RequisiteFactory {

    /**
     * Creates the proper requisite corresponding with the given text
     * (Currently doesn't address credit requirement requisites)
     * @param reqText used to identify the type of requisite
     * @return a subclass of Requisite with the reqText
     */
    public static Requisite getRequisite(String reqText){
        if(CourseList.isCourseID(reqText)){
            return new CourseReq(reqText);

        }

        Department tempDepartment = DepartmentList.matchDepartment(reqText);
        if(reqText.contains("PERMISSION") && tempDepartment != null){
            return new DepartmentReq(tempDepartment.getDepartment());
        }

        return new OtherReq(reqText);
    }
}
