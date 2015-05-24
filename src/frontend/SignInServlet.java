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
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.JsonInterpreterFromRequest.getJSONFromRequest;
import static utils.PageGenerator.getPage;

public class SignInServlet extends HttpServlet {

    final private Logger logger = getLogger(SignInServlet.class.getName());
    final private AccountService accountService;
    final private Context context;

    public SignInServlet(Context contextGlobal) {
        this.accountService = (AccountService) contextGlobal.get(AccountService.class);
        context = contextGlobal;
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        logger.info(LoggerMessages.doGetStart());
        logger.info(LoggerMessages.requestGetParams(), request.getParameterMap().toString());
        response.setStatus(SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        String loginStatus;

        if (!context.isBlocked()) {
            if (user == null) {
                logger.info(LoggerMessages.signIn());
                loginStatus = Messages.logInStatus();
                pageToReturn = "signInForm.html";
            } else {
                logger.info(LoggerMessages.alreadyLoggedIn(), user.getLogin());
                loginStatus = Messages.alreadyLoggedIn();
                pageToReturn = "authstatus.html";
            }
        } else {
            loginStatus = Messages.block();
            logger.info(LoggerMessages.block());
            pageToReturn = "authstatus.html";
        }

        pageVariables.put("loginStatus", loginStatus);

        response.getWriter().println(getPage(pageToReturn, pageVariables));
        logger.info(LoggerMessages.doGetFinish());
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
            if (!accountService.isSessionWithSuchLoginExist(login)) {
                UserProfile profile = accountService.getUser(login);
                if (profile != null && profile.getPassword().equals(password)) {
                    accountService.addSessions(session.getId(), profile);
                    status = 200;
                    logger.info(LoggerMessages.hasAuthorised(), profile.getLogin());
                } else {
                    status = 400;
                    logger.info(LoggerMessages.wrongPasOrLogin(), password, login);
                    message = Messages.wrongPasOrLogin();
                }
            } else {
                status = 400;
                logger.info(LoggerMessages.alreadyLoggedIn(), login);
                message = Messages.alreadyLoggedIn();
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
