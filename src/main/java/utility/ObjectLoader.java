package utility;

import java.io.*;


/**
 * Utility class which allows the client to load CourseList
 *
 * @author JohnN05
 */

public class ObjectLoader {

    /**
     * Allows the client to save Objects to a specific file path
     * @param o Object being saved
     * @param filePath location where Object is saved
     */
    public static void save(Object o, String filePath){
        ObjectOutputStream oOut;
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(filePath);
            oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(o);

            oOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the client to load Objects from a specific file path
     * @param filePath located where Object is being loaded
     * @return the Object located at the filePath
     */
    public static Object load(String filePath){
        ObjectInputStream objectStream;
        try{
            FileInputStream fIn = new FileInputStream(filePath);
            objectStream = new ObjectInputStream(fIn);

            return objectStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
