package utils;

/**
 * Created by neikila on 31.03.15.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import resource.LoggerMessages;
import resource.ResourceFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class JsonInterpreterFromRequest
{
    static final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    static final Logger logger = LogManager.getLogger(JsonInterpreterFromRequest.class.getName());

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
            logger.info(loggerMessages.jsonGotFromRequest(), jsonObj.toString());
        } catch (Exception e) { //сообщение об ошибке
            e.printStackTrace();
            logger.error(e);
            logger.error(loggerMessages.errorInReadingJSON());
        }
        return jsonObj;
    }


    static public JSONObject getJsonFromString(String request) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(request);
            logger.info(loggerMessages.jsonGotFromRequest(), obj.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e);
            logger.error(loggerMessages.errorInReadingJSON());
        }
        return (JSONObject) obj;
    }
}