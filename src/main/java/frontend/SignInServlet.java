package frontend;

import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import Interface.AccountService;
import main.user.UserProfile;
import resource.LoggerMessages;
import resource.ResourceFactory;
import utils.JsonInterpreterFromRequest;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {

    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    final private Logger logger = LogManager.getLogger(SignInServlet.class.getName());
    final private AccountService accountService;
    final private Context context;

    public SignInServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
        context = contextGlobal;
    }

    protected void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        logger.info(loggerMessages.doGetStart());
        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        String loginStatus;

        if (!context.isBlocked()) {
            if (user == null) {
                logger.info(loggerMessages.signIn());
                loginStatus = "Log In:";
                pageToReturn = "signInForm.html";
            } else {
                logger.info(loggerMessages.alreadyLoggedIn(), user.getLogin());
                loginStatus = "You have already logged in";
                pageToReturn = "authstatus.html";
            }
        } else {
            loginStatus = "Auth is blocked";
            logger.info(loggerMessages.block());
            pageToReturn = "authstatus.html";
        }

        pageVariables.put("loginStatus", loginStatus);

        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        logger.info(loggerMessages.doGetFinish());
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        logger.info(loggerMessages.doPostStart());

        JSONObject jsonObject = JsonInterpreterFromRequest.getJSONFromRequest(request);

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
                    logger.info(loggerMessages.hasAuthorised(), profile.getLogin());
                } else {
                    status = 400;
                    logger.info(loggerMessages.wrongPasOrLogin(), password, login);
                    message = "Wrong";
                }
            } else {
                status = 400;
                logger.info(loggerMessages.alreadyLoggedIn(), login);
                message = "Already";
            }
        } else {
            status = 400;
            logger.info(loggerMessages.block());
            message = "Blocked";
        }

        createResponse(response, status, message, login);
        logger.info(loggerMessages.doPostFinish());
    }

    private void createResponse(HttpServletResponse response, short status, String errorMessage, String login) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            data.put("message", errorMessage);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            data.put("login", login);
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}
