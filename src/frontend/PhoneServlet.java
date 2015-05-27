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
import static utils.JsonInterpreterFromRequest.getJSONFromRequest;

public class PhoneServlet extends HttpServlet {

    final private Logger logger = getLogger(PhoneServlet.class.getName());
    final private AccountService accountService;
    final private Context context;

    public PhoneServlet(Context contextGlobal) {
        this.accountService = (AccountService) contextGlobal.get(AccountService.class);
        context = contextGlobal;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        logger.info(LoggerMessages.doPostStart());
        logger.info(LoggerMessages.requestGetParams(), request.getParameterMap().toString());
        JSONObject jsonObject = getJSONFromRequest(request);
        logger.info(LoggerMessages.jsonGotFromRequest(), jsonObject.toString());
        String login = (String) jsonObject.get("login");
        String password = (String) jsonObject.get("password");

        HttpSession session = request.getSession();

        String message = "";
        short status;

        if (!context.isBlocked()) {
            if (accountService.isSessionWithSuchLoginExist(login)) {
                if (!accountService.isPhoneSessionWithSuchLoginExist(login)) {
                    UserProfile profile = accountService.getUser(login);
                    if (profile != null && profile.getPassword().equals(password)) {
                        status = 200;
                        accountService.addPhoneSession(session.getId(), login);
                        logger.info("{} successfully signed in with phone", profile.getLogin());
                    } else {
                        status = 400;
                        logger.info(LoggerMessages.wrongPasOrLogin(), password, login);
                        message = Messages.wrongPasOrLogin();
                    }
                } else {
                    status = 400;
                    logger.info("{} is already using phone", login);
                    message = "Someone is already using phone for this account";
                }
            } else {
                status = 400;
                logger.info("User hasn't athorized via the main window");
                message = "User hasn't athorized via the main window";
            }
        } else {
            status = 400;
            logger.info(LoggerMessages.block());
            message = Messages.block();
        }

        createResponse(response, status, message, login);
        logger.info(LoggerMessages.doPostFinish());
    }

    private void createResponse(HttpServletResponse response, short status, String errorMessage, String login) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(SC_OK);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            data.put("message", errorMessage);
        } else {
            data.put("login", login);
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}
