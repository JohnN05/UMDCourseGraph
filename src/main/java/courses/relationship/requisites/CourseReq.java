package courses.relationship.requisites;

/**
 * Represents a Course that is also a Prerequisite
 *
 * @author JohnN05
 */
public class CourseReq extends Requisite{
    private final String course_id;

    public CourseReq(String course_id){
        super(ReqType.COURSE);
        this.course_id = course_id;
    }

    @Override
    public String getInfo() {
        return course_id;
    }
}
