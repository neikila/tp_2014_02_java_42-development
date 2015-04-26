package frontend;

import Interface.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import resource.LoggerMessages;
import resource.Messages;
import resource.ResourceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProfileServlet extends HttpServlet {

    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    final private Logger logger = LogManager.getLogger(ProfileServlet.class.getName());
    final private AccountService accountService;
    final private Messages messages = (Messages) ResourceFactory.instance().getResource("messages");

    public ProfileServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        logger.info(loggerMessages.doGetStart());
        logger.info(loggerMessages.requestGetParams(), request.getParameterMap().toString());
        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());

        short status;
        String message = "";

        if (user == null) {
            logger.info(loggerMessages.notAuthorised());
            status = 401;
            message = messages.notAuthorised();
        } else {
            logger.info(loggerMessages.hasAuthorised(), user.getLogin());
            status = 200;
        }

        createResponse(response, status, message, user);
        logger.info(loggerMessages.doGetFinish());
    }

    private void createResponse(HttpServletResponse response, short status, String errorMessage, UserProfile user) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", errorMessage);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            String pass = user.getPassword();
            final String newPass = pass.substring(0, (pass.length() - 3)).replaceAll(".", "*") + pass.substring((pass.length() - 3), pass.length());
            data.put("password", newPass);
            data.put("login", user.getLogin());
            data.put("email", user.getEmail());
            data.put("role", user.getRole());
            data.put("score", user.getScore());
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}