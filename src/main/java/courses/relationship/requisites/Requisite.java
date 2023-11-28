package courses.relationship.requisites;

import java.util.Objects;

/**
 * Represents a requisite.
 *
 * @author JohnN05
 */
public abstract class Requisite{
    private final ReqType type;

    public Requisite(ReqType type){
        this.type = type;
    }

    public abstract String toString();

    public ReqType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requisite requisite = (Requisite) o;
        return type == requisite.type && this.toString().equals(requisite.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type) * toString().hashCode();
    }
}
