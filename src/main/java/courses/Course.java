package courses;

import courses.relationship.requisites.Requisite;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * courses.Course class is used to represent every UMD course.
 * Stores information on a course such as Prerequisites, Restrictions, etc.
 *
 * @author JohnN05
 */
public class Course implements Comparable<Course>, Serializable {
    private final String course_id;
    private final String name;
    private final int credits;
    private final String description;
    private final Set<Set<String>> gen_eds;
    private final HashSet<HashSet<Requisite>> prereqs;
    private final HashSet<HashSet<Requisite>> coreqs;


    public Course(String course_id, String name, int credits, String description, Set<Set<String>> gen_eds, HashSet<HashSet<Requisite>> prereqs, HashSet<HashSet<Requisite>> coreqs) {
        this.course_id = course_id;
        this.name = name;
        this.credits = credits;
        this.description = description;
        this.gen_eds = gen_eds;
        this.prereqs = prereqs;
        this.coreqs = coreqs;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    public Set<Set<String>> getGen_eds() {
        return gen_eds;
    }

    public HashSet<HashSet<Requisite>> getPrereqs() {
        return prereqs;
    }

    public HashSet<HashSet<Requisite>> getCoreqs() {
        return coreqs;
    }

    @Override
    public String toString() {
        return "courses.Course{" +
                "course_id='" + course_id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                ", gen_eds=" + gen_eds +
                ", prereqs=" + prereqs +
                ", corereqs=" + coreqs +
                '}';
    }

    @Override
    public int compareTo(Course o) {
        return this.course_id.compareTo(o.course_id);
    }
}
