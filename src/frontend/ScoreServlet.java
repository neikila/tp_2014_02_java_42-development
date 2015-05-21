package frontend;

import main.Context;
import main.accountService.AccountService;
import main.user.UserProfile;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.LoggerMessages;
import utils.Messages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.logging.log4j.LogManager.getLogger;

public class ScoreServlet extends HttpServlet {

    final private Logger logger = getLogger(ScoreServlet.class.getName());
    final private AccountService accountService;

    public ScoreServlet(Context contextGlobal) {
        this.accountService = (AccountService) contextGlobal.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        logger.info(LoggerMessages.doGetStart());
        logger.info(LoggerMessages.requestGetParams(), request.getParameterMap().toString());
        short status = 200;
        String message = "";
        String limitInRequest = request.getParameter("limit");
        int limit;
        if (limitInRequest == null) {
            logger.info(LoggerMessages.lackOfParam(), "limit");
            limit = 0;
            message = Messages.wrongLimit();
            status = 400;
        } else {
            try {
                limit = parseInt(request.getParameter("limit"));
            } catch (Exception e) {
                logger.error(e);
                logger.error(LoggerMessages.paramHasWrongType(), "limit");
                limit = 0;
                message = Messages.wrongLimit();
                status = 400;
            }
        }
        createResponse(response, status, message, accountService.getFirstPlayersByScore(limit));
        logger.info(LoggerMessages.doGetFinish());
    }

    private void createResponse(HttpServletResponse response, short status, String errorMessage, List<UserProfile> FirstLimit) throws IOException {

        response.setStatus(SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray scoreList = new JSONArray();
        JSONObject scoreItem;

        if (status != 200) {
            data.put("message", errorMessage);
        } else {
            for (UserProfile aFirstLimit : FirstLimit) {
                scoreItem = new JSONObject();
                scoreItem.put("login", aFirstLimit.getLogin());
                scoreItem.put("score", aFirstLimit.getScore());
                scoreList.add(scoreItem);
            }
            data.put("scoreList", scoreList);
        }
        obj.put("data", data);
        obj.put("status", status);

        response.getWriter().write(obj.toString());
    }
}