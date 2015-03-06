package frontend;

import org.json.simple.JSONObject;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        String loginStatus;

        if (user == null) {
            loginStatus = "Log In:";
            pageToReturn = "signInForm.html";
        } else {
            loginStatus = "You have already logged in";
            pageToReturn = "authstatus.html";
        }

        pageVariables.put("loginStatus", loginStatus);

        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn = "authstatus.html";

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();

        String message;

        if (!accountService.isSessionWithSuchLoginExist(login)) {
            UserProfile profile = accountService.getUser(login);
            if (profile != null && profile.getPassword().equals(password)) {
                accountService.addSessions(session.getId(), profile);
                message = "Login passed";
            } else {
                message = "Wrong login/password! Try again.";
            }
        } else {
            message = "User with such login is already online.";
        }
        pageVariables.put("loginStatus", message);
//text/x-json
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        int status = 200;
        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("message", message);
        obj.put("data", data);
        obj.put("status", status);
        try {
            response.getWriter().write(obj.toString());
        } catch (IOException e) {

        }
    }
}
