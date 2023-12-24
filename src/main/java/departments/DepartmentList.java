package departments;

import utility.HttpReader;

import java.util.List;

/**
 * Provides the client with a set of UMD's departments with umd.io API
 *
 * @author JohnN05
 */

public class DepartmentList {
    private static final List<Department> allDepartments;

    static{
        allDepartments = HttpReader.requestDepartmentSet();
    }

    public static List<Department> getAllDepartments() {
        return allDepartments;
    }

    /**
     * Matches the corresponding Department with the text
     * @param text Used to match with Departments
     * @return Corresponding Department.  Returns null if no match is found.
     */
    public static Department matchDepartment(String text){
        for(Department department: DepartmentList.getAllDepartments()) {
            String departmentID = department.getDept_id();
            if (text.contains(departmentID)) {
                return department;
            }
        }
        return null;
    }
}
