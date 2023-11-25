package courses.relationship.requisites;

/**
 * Represents a Department that is also a Requisite.
 *
 * @author JohnN05
 */

public class DepartmentReq extends Requisite{
    private final String department;

    public DepartmentReq(String department){
        super(ReqType.DEPARTMENT);
        this.department = department;
    }

    @Override
    public String getInfo() {
        return department;
    }
}
