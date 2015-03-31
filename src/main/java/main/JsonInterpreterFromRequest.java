package main;

/**
 * Created by neikila on 31.03.15.
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class JsonInterpreterFromRequest
{
    static public JSONObject getJSONFromRequest(HttpServletRequest request, String servletName) {
        JSONObject jsonObj = null;
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
            String test = jb.toString();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(test);
            jsonObj = (JSONObject) obj;
        } catch (Exception e) { //сообщение об ошибке
            System.out.println("Error while getting the JSON in" + servletName);
        }
        return jsonObj;
    }
}