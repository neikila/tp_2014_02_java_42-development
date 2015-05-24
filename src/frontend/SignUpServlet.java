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
import static main.user.MyValidator.*;
import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.JsonInterpreterFromRequest.getJSONFromRequest;
import static utils.PageGenerator.getPage;

public class SignUpServlet extends HttpServlet {

    final private Logger logger = getLogger(SignUpServlet.class.getName());
    final private AccountService accountService;
    final private Context context;

    public SignUpServlet(Context contextGlobal) {
        this.accountService = (AccountService) contextGlobal.get(AccountService.class);
        context = contextGlobal;
    }

    // Для демонстрации! Ну, и, отладки
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        logger.info(LoggerMessages.doGetStart());
        logger.info(LoggerMessages.requestGetParams(), request.getParameterMap().toString());
        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        String pageToReturn;
        String message;
        Map<String, Object> pageVariables = new HashMap<>();
        if (!context.isBlocked()) {
            if (user == null) {
                pageToReturn = "signUpForm.html";
                message = Messages.fillAllTheGaps();
                logger.info(LoggerMessages.signUp());
            } else {
                pageToReturn = "signupstatus.html";
                message = Messages.logOutFirst();
                logger.info(LoggerMessages.alreadyLoggedIn(), user.getLogin());
            }
        } else {
            pageToReturn = "signupstatus.html";
            message = Messages.block();
            logger.info(LoggerMessages.block());
        }
        pageVariables.put("signUpStatus", message);
        response.getWriter().println(getPage(pageToReturn, pageVariables));
        response.setStatus(SC_OK);
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
        String email = (String) jsonObject.get("email");

        UserProfile user = new UserProfile(login, password, email);
        HttpSession session = request.getSession();

        String message = "";
        short status;

        if (!context.isBlocked()) {
            if (accountService.getSessions(session.getId()) == null) {
                if (isUserNameValid(login) && isPasswordValid(password) && isEmailValid(email)) {
                    if (accountService.addUser(login, user)) {
                        status = 200;
                        logger.info(LoggerMessages.signUpSuccess(), user.getLogin());
                        accountService.addSessions(session.getId(), user);
                        logger.info(LoggerMessages.hasAuthorised(), user.getLogin());
                    } else {
                        status = 400;
                        message = Messages.exist();
                        logger.info(LoggerMessages.loginIsAlreadyExist(), login);
                    }
                } else {
                    status = 400;
                    message = Messages.wrongSignUpData();
                    logger.info(LoggerMessages.wrongSignUpData(), login, email, password);
                }
            } else {
                status = 400;
                message = Messages.alreadyLoggedIn();
                logger.info(LoggerMessages.alreadyLoggedIn());
            }
        } else {
            logger.info(LoggerMessages.block());
            status = 400;
            message = Messages.block();
        }
        createResponse(response, status, message, user);
        logger.info(LoggerMessages.doPostFinish());
    }


    private void createResponse(HttpServletResponse response, short status, String message, UserProfile user) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(SC_OK);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            data.put("message", message);
        } else {
            String pass = user.getPassword();
            data.put("password", pass.substring(0, (pass.length() - 3)).replaceAll(".", "*") + pass.substring((pass.length() - 3), pass.length()));
            data.put("login", user.getLogin());
            data.put("email", user.getEmail());
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}