package utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import courses.RawCourse;
import departments.Department;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utility class which handles the API requests.
 *
 * @author JohnN05
 */

public class HttpReader {
    private static final String DEPARTMENT_URL = "https://api.umd.io/v1/courses/departments";
    private static final String COURSE_LIST_API = "https://api.umd.io/v1/courses/list";
    private static final String COURSE_API = "https://api.umd.io/v1/courses";
    public static final int PER_PAGE = 100;

    private static final Gson gson = new Gson();

    /**
     * Requests a list of RawCourses from umd.io
     *
     * @param course_id the course_id of the RawCourses that will be returned.
     * @return the list of RawCourses
     */
    public static List<RawCourse> requestRawCourse(String course_id){
        String url = COURSE_API+ "/" + course_id;
        return gson.fromJson(getRequest(url).body(), new TypeToken<ArrayList<RawCourse>>(){}.getType());
    }

    public static List<RawCourse> requestRawCoursePage(int pageNum){
        String additionalQueries =  "?per_page=" + PER_PAGE + "&page=" + pageNum;
        String url = COURSE_API + additionalQueries;
        return gson.fromJson(getRequest(url).body(), new TypeToken<ArrayList<RawCourse>>(){}.getType());
    }

    /**
     * Requests a list of minified Courses from umd.io
     *
     * @return the list of Courses
     */
    public static List<RawCourse> requestMinifiedCourses(){
        return gson.fromJson(getRequest(COURSE_LIST_API).body(), new TypeToken<ArrayList<RawCourse>>(){}.getType());
    }

    /**
     * Requests the set of departments from umd.io
     *
     * @return the complete department set
     */
    public static Set<Department> requestDepartmentSet(){
        return gson.fromJson(getRequest(DEPARTMENT_URL).body(), new TypeToken<TreeSet<Department>>(){}.getType());
    }


    /**
     * Sends get requests to an API
     *
     * @param url the API where the request is being sent
     * @return the response to the get request
     */
    private static HttpResponse<String> getRequest(String url){

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest getRequest = null;
        HttpResponse<String> getResponse = null;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
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

        return getResponse;
    }
}
