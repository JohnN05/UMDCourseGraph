import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Course class is used to represent every UMD course.
 * Stores information on a course such as Prerequisites, Restrictions, etc.
 *
 * @author JohnN05
 */
public class Course implements Comparable<Course>{

    //Keywords used to help interpret course requisites
    private static final HashMap<String, String> KEYWORDS = new HashMap<>();
    private static final String[] SPECIAL_KEYWORDS = {"PERMISSION"};
    private static final String[] CONJUNCTIONS = {"OR", "AND"};

    private static final String DEFAULT_SEARCH_QUERIES = "&_openSectionsOnly=on&instructor=&_facetoface=on&_blended=on&_online=on&courseStartCompare=&courseStartHour=&courseStartMin=&courseStartAM=&courseEndHour=&courseEndMin=&courseEndAM=&teachingCenter=ALL";
    private static final int COURSE_NAME_LENGTH = 7;  //length of Course IDs
    private static final Gson gson = new Gson();

    private static TreeSet<Department> allDepartments = new TreeSet<>();

    static{
        KEYWORDS.put("EARNED", "EXAM"); //for prerequisite exams
        KEYWORDS.put("HIGH","COURSE");  //for high school prerequisite
    }

    /**
     * Gets all the UMD departments and puts them in a TreeSet
     */
    static{
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest getRequest = null;
        HttpResponse<String> getResponse = null;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.umd.io/v1/courses/departments"))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allDepartments = gson.fromJson(getResponse.body(), new TypeToken<TreeSet<Department>>(){}.getType());
    }

    @Override
    public int compareTo(Course o) {
        return 0;
    }
}
