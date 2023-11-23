package courses.relationship.requisites;

/**
 * Represents a credit requirement requisite
 *
 * @author JohnN05
 */

public class CreditReq extends Requisite{
    private final int credits;  //the amount of credits to satisfy the prerequisite
    private final String department;  //the department which the credits must be in

    public CreditReq(int credits, String department){
        super(ReqType.CREDIT);
        this.credits = credits;
        this.department = department;
    }

    public int getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }
}
