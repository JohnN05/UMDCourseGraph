package courses.relationship.requisites;

/**
 * Represents a requisite.
 *
 * @author JohnN05
 */
public class Requisite {
    private final ReqType type;

    public Requisite(ReqType type){
        this.type = type;
    }

    public ReqType getType() {
        return type;
    }

}
