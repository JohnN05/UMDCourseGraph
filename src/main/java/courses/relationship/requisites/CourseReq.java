package courses.relationship.requisites;

/**
 * Represents a Course that is also a Prerequisite
 *
 * @author JohnN05
 */
public class CourseReq extends Requisite{
    private String course_id;

    public CourseReq(String course_id){
        super(ReqType.COURSE);
        course_id = this.course_id;
    }

    public String getCourse_id() {
        return course_id;
    }
}
