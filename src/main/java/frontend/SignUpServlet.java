package frontend;

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

/**
 * Created by v.chibrikov on 13.09.2014.
 */
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

        UserProfile user = new UserProfile(login, password, email, server);
        HttpSession session = request.getSession();

        String pageToReturn = "signupstatus.html";

        Map<String, Object> pageVariables = new HashMap<>();

        String message = "";

        // В случае успешной регистрации производиться автоматическая авторизация и возврат сообщения об успешно регистрации
        // Можно переделать на возврат формы профиля
        if (accountService.addUser(login, user)) {
            accountService.addSessions(session.getId(), user);
            message = "New user created";;
        } else {
            // В случае, если такой логин уже занят, возвращается соответствующее сообщение и повторная форма регистрации
            message = "User with login: " +
                    "" + login + " already exists. Try again.";
            pageToReturn = "signUpForm.html";
        }

        pageVariables.put("signUpStatus", message);
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}