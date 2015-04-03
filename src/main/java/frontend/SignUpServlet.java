package frontend;

import Interface.AccountService;
import main.Context;
import main.MyValidator;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpServlet extends HttpServlet {

    final static private Logger logger = LogManager.getLogger(SignOutServlet.class.getName());
    final private AccountService accountService;
    final private Context context;

    public SignUpServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
        context = contextGlobal;
    }

    // Для демонстрации! Ну, и, отладки
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet Start");
        HttpSession session = request.getSession();
        UserProfile profile = accountService.getSessions(session.getId());

        String pageToReturn;
        String message;
        Map<String, Object> pageVariables = new HashMap<>();
        if (!context.isBlocked()) {
            if (profile == null) {
                pageToReturn = "signUpForm.html";
                message = "Fill all gaps, please:";
                logger.info("Not enough arguments");
            } else {
                pageToReturn = "signupstatus.html";
                message = "You have to logout before signing up.";
                logger.info("User is logged in");
            }
        } else {
            pageToReturn = "signupstatus.html";
            message = "Signing up is blocked.";
            logger.info("Autherization is blocked");
        }
        pageVariables.put("signUpStatus", message);
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
        logger.info("doGet Success");
    }


    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost Start");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        //TODO заменить на прием JSON (пока не трогаю, чтобы было удобно показывать)

        UserProfile user = new UserProfile(login, password, email);
        HttpSession session = request.getSession();

        String message = "";
        short status;

        if (!context.isBlocked()) {
            if (accountService.getSessions(session.getId()) == null) {
                if (MyValidator.isUserNameValid(login) && MyValidator.isPasswordValid(password) && MyValidator.isEmailValid(email)) {
                    if (accountService.addUser(login, user)) {
                        accountService.addSessions(session.getId(), user);
                        status = 200;
                    } else {
                        status = 400;
                        message = "Exist";
                        logger.info("User with such login {} is already exist", login);
                    }
                } else {
                    status = 400;
                    message = "Wrong";
                    logger.info("Wrong login: {} or email: {} or password: {}", login, email, password);
                }
            } else {
                status = 400;
                message = "Already";
                logger.info("User is logged in");
            }
        } else {
            logger.info("Autherization is blocked");
            status = 400;
            message = "Blocked";
        }
        createResponse(response, status, message, user);
        logger.info("doPost Success");
    }


    private void createResponse (HttpServletResponse response, short status, String message, UserProfile user) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            data.put("message", message);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
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