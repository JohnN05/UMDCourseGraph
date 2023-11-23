package departments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import utility.HttpReader;

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
    private static Set<Department> allDepartments;

    static{
        allDepartments = HttpReader.requestDepartmentSet();
    }

    public static Set<Department> getAllDepartments() {
        return allDepartments;
    }
}
