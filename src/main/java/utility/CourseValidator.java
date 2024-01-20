package utility;

import departments.DepartmentList;

/**
 * Checks if part of a Course if valid
 *
 * @author JohnN05
 */

public class CourseValidator {
    private static final int COURSE_ID_LENGTH = 7;

    /**
     * Checks if the text fits the description of a Course ID
     *
     * @param text the text being checked
     * @return true if first four characters are a Department while the last three characters are numbers.
     * Otherwise, returns false.
     */
    public static boolean isCourseID(String text){

        if(text.length() != COURSE_ID_LENGTH || DepartmentList.matchDepartment(text.substring(0, 4)) == null){
            return false;
        }else {

            try {
                Integer.parseInt(text.substring(4));
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
