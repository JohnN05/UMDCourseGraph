package utility;

import courses.CourseList;

import java.io.*;


/**
 * Utility class which allows the client to load CourseList
 *
 * @author JohnN05
 */

public class CourseListLoader {

    /**
     * Allows the client to save CourseList to a specific file path
     * @param list CourseList being saved
     * @param filePath location where CourseList is saved
     */
    public static void saveList(CourseList list, String filePath){
        ObjectOutputStream oOut;
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(filePath);
            oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(list);

            oOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the client to load CourseList from a specific file path
     * @param filePath located where CourseList is being loaded
     * @return the CourseList located at the filePath
     */
    public static CourseList loadList(String filePath){
        ObjectInputStream objectStream;
        try{
            FileInputStream fIn = new FileInputStream(filePath);
            objectStream = new ObjectInputStream(fIn);

            return (CourseList)objectStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
