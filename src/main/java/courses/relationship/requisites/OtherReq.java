package courses.relationship.requisites;

/**
 * Represents a Requisite that doesn't fit other Requisite types
 *
 * @author JohnN05
 */

public class OtherReq extends Requisite{
    private final String text;

    public OtherReq(String text){
        super(ReqType.OTHER);
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
