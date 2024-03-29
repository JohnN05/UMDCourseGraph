package utility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
     * Requests a RawCourse from umd.io
     *
     * @param course_id the course_id of the RawCourse that will be returned.
     * @return the RawCourse requested
     */
    public static RawCourse requestRawCourse(String course_id){
        String url = COURSE_API+ "/" + course_id;
        List<RawCourse> temp = gson.fromJson(getRequest(url), new TypeToken<ArrayList<RawCourse>>(){}.getType());
        return temp.get(0);
    }

    public static List<RawCourse> requestRawCoursePage(int pageNum) throws IOException {
        String additionalQueries =  "?per_page=" + PER_PAGE + "&page=" + pageNum;
        String url = COURSE_API + additionalQueries;
        try {
            return gson.fromJson(getRequest(url), new TypeToken<ArrayList<RawCourse>>() {
            }.getType());
        }catch(JsonSyntaxException e){
            throw new IOException("Failed to access " + COURSE_API);
        }
    }

    /**
     * Requests a list of minified Courses from umd.io
     *
     * @return the list of Courses
     */
    public static List<RawCourse> requestMinifiedCourses(){
        return gson.fromJson(getRequest(COURSE_LIST_API), new TypeToken<ArrayList<RawCourse>>(){}.getType());
    }

    /**
     * Requests the set of departments from umd.io
     *
     * @return the complete department set
     */
    public static List<Department> requestDepartmentSet(){
        return gson.fromJson(getRequest(DEPARTMENT_URL), new TypeToken<ArrayList<Department>>(){}.getType());
    }


    /**
     * Sends get requests to an API
     *
     * @param url the API where the request is being sent
     * @return the response to the get request
     */
    private static String getRequest(String url){

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

        //removes parenthesis that may cause escape sequence errors
        assert getResponse != null;
        return getResponse.body().replace("(","").replace(")","");
    }
}
