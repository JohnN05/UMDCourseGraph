/**
 * Department class is used to represent all departments at UMD.
 *
 * @author JohnN05
 */
public class Department implements Comparable<Department>{
    private String dept_id;
    private String department;

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String toString(){
        return dept_id + " - " + department;
    }

    @Override
    public int compareTo(Department o) {
        return this.dept_id.compareTo(o.dept_id);
    }
}