package utils;

/**
 * Created by neikila on 31.03.15.
 */

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.LoggerMessages.errorInReadingJSON;

public class JsonInterpreterFromRequest {
    static final Logger logger = getLogger(JsonInterpreterFromRequest.class.getName());

    static public JSONObject getJSONFromRequest(HttpServletRequest request) {
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
            logger.error(e);
            logger.error(errorInReadingJSON());
        }
        return jsonObj;
    }


    static public JSONObject getJsonFromString(String request) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(request);
        } catch (ParseException e) {
            logger.error(e);
            logger.error(errorInReadingJSON(), request);
        }
        return (JSONObject) obj;
    }
}