package frontend;

import main.AccountService;
import main.MyValidator;
import main.UserProfile;
import org.json.simple.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    // Действие, которое производиться, если пришел запрос метода Get
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserProfile profile = accountService.getSessions(session.getId());

        String pageToReturn;
        String message;
        Map<String, Object> pageVariables = new HashMap<>();

        // Если пользователь на авторизован, то возвращается форма регистрации
        if (profile == null) {
            pageToReturn = "signUpForm.html";
            message = "Fill all gaps, please:";
        } else {
            // В противном случае возвращается сообщение о том, что нельзя зарегистрироваться, не выполнив logout
            pageToReturn = "signupstatus.html";
            message = "You have to logout before signing up.";
        }
        pageVariables.put("signUpStatus", message);
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String server = request.getParameter("server");
        String email = request.getParameter("email");

        //TODO заменить на прием JSON (пока не трогаю, чтобы было удобно показывать)

        UserProfile user = new UserProfile(login, password, email, server);
        HttpSession session = request.getSession();

        String message = "";
        short status;

        if (accountService.getSessions(session.getId()) == null) {
            if (MyValidator.isUserNameValid(login) && MyValidator.isPasswordValid(password) && MyValidator.isEmailValid(email)) {
                if (accountService.addUser(login, user)) {
                    accountService.addSessions(session.getId(), user);
                    status = 200;
                } else {
                    status = 400;
                    message = "Exist";
                }
            } else {
                status = 400;
                message = "Wrong";
            }
        } else {
            status = 400;
            message = "Already";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            data.put("message", message);
        } else {
            String pass = user.getPassword();
            data.put("password", pass.substring(0, (pass.length() - 3)).replaceAll(".", "*") + pass.substring((pass.length() - 3), pass.length()));
            data.put("login", login);
            data.put("email", email);
            data.put("server", server);
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}