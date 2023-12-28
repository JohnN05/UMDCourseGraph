package departments;

import utility.HttpReader;
import utility.ObjectLoader;

import java.util.List;

import static utility.ObjectLoader.load;

/**
 * Provides the client with a set of UMD's departments with umd.io API
 *
 * @author JohnN05
 */

public class DepartmentList {
    private static final List<Department> allDepartments;
    private static final String OFFLINE_LIST_PATH = "offline_department_list";

    static{
        List<Department> list;

        try {
            list = HttpReader.requestDepartmentSet();
            ObjectLoader.save(list, OFFLINE_LIST_PATH);

        //allows offline access when network is unavailable
        }catch(RuntimeException e){
            list = (List<Department>) load(OFFLINE_LIST_PATH);
        }
        allDepartments = list;
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
