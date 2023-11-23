package departments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.TreeSet;

/**
 * Provides the client with a set of UMD's departments with umd.io API
 *
 * @author JohnN05
 */

public class DepartmentList {
    private static final String API_URL = "https://api.umd.io/v1/courses/departments";
    private static Set<Department> allDepartments;

    static{
        Gson gson = new Gson();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest getRequest = null;
        HttpResponse<String> getResponse = null;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
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

    public static Set<Department> getAllDepartments() {
        return allDepartments;
    }
}
