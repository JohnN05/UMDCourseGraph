/**
 * Class that implements CourseRelationship to handle relationships that don't fit in Department or Course
 *
 * @author JohnN05
 */
public class otherRelationship implements CourseRelationship{
    private String data;

    public otherRelationship(String data){
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
