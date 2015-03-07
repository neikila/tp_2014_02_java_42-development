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


    //Для демонстрации!
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


        //TODO заменить на прием JSON (пока не трогаю, чтобы было удобно показывать)

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        String message = "";
        short status;

        if (!accountService.isSessionWithSuchLoginExist(login)) {
            UserProfile profile = accountService.getUser(login);
            if (profile != null && profile.getPassword().equals(password)) {
                accountService.addSessions(session.getId(), profile);
                status = 200;
            } else {
                status = 400;
                message = "Wrong";
            }
        } else {
            status =  400;
            message = "Already";
        }

        createResponse(response, status, message, login);
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
