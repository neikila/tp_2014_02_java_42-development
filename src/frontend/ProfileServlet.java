package frontend;

import main.Context;
import main.accountService.AccountService;
import main.user.UserProfile;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import utils.LoggerMessages;
import utils.Messages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.logging.log4j.LogManager.getLogger;

public class ProfileServlet extends HttpServlet {

    final private Logger logger = getLogger(ProfileServlet.class.getName());
    final private AccountService accountService;

    public ProfileServlet(Context contextGlobal) {
        this.accountService = (AccountService) contextGlobal.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        logger.info(LoggerMessages.doGetStart());
        logger.info(LoggerMessages.requestGetParams(), request.getParameterMap().toString());
        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());

        short status;
        String message = "";

        if (user == null) {
            logger.info(LoggerMessages.notAuthorised());
            status = 401;
            message = Messages.notAuthorised();
        } else {
            logger.info(LoggerMessages.hasAuthorised(), user.getLogin());
            status = 200;
        }

        createResponse(response, status, message, user);
        logger.info(LoggerMessages.doGetFinish());
    }

    private void createResponse(HttpServletResponse response, short status, String errorMessage, UserProfile user) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(SC_OK);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            data.put("message", errorMessage);
        } else {
            String pass = user.getPassword();
            final String newPass = pass.substring(0, (pass.length() - 3)).replaceAll(".", "*") + pass.substring((pass.length() - 3), pass.length());
            data.put("password", newPass);
            data.put("login", user.getLogin());
            data.put("email", user.getEmail());
            data.put("role", user.getRoleName());
            data.put("score", user.getScore());
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}