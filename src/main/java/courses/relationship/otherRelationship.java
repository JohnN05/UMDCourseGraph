package courses.relationship;

import courses.relationship.CourseRelationship;

/**
 * Class that implements courses.relationship.CourseRelationship to handle relationships that don't fit in departments.Department or courses.Course
 *
 * @author JohnN05
 */
public class otherRelationship implements CourseRelationship {
    private String data;

    public otherRelationship(String data){
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
