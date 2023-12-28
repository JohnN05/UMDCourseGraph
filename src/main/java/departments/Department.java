package departments;

import java.io.Serializable;

/**
 * Department class is used to represent a departments at UMD.
 *
 * @author JohnN05
 */
public class Department implements Comparable<Department>, Serializable {
    private final String dept_id;
    private final String department;

    public Department(String dept_id, String department) {
        this.dept_id = dept_id;
        this.department = department;
    }

    public String getDept_id() {
        return dept_id;
    }

    public String getDepartment() {
        return department;
    }


    public String toString(){
        return dept_id + " - " + department;
    }

    @Override
    public int compareTo(Department o) {
        return this.dept_id.compareTo(o.dept_id);
    }
}