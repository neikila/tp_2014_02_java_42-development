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

public class LogOutServlet extends HttpServlet {

    private AccountService accountService;

    public LogOutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());

        if (user == null)
        {
            pageVariables.put("loginStatus", "You haven't Logged In:");
            pageToReturn = "signInForm.html";
        } else {
            pageToReturn = "logOut.html";
            pageVariables.put("logOutStatus", "Are you sure you want to exit?");
        }

        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String answer = request.getParameter("answer");

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();

        UserProfile profile = accountService.getSessions(session.getId());

        // В случае, если пользователь подтверждает logout, удаляем пользователя из сессии и возвращаем страницу авторизации
        if (answer.equals("Yes") || profile == null) {
            accountService.removeSession(session.getId());
            pageVariables.put("loginStatus", "Log In:");
            pageToReturn = "signInForm.html";
        } else {
            // В случае отказа возвращаем страницу профиля
            pageToReturn = "profile.html";
            pageVariables.put("login", profile.getLogin());
            pageVariables.put("password", profile.getPassword());
            pageVariables.put("email", profile.getEmail());
            pageVariables.put("server", profile.getServer());
            pageVariables.put("role", profile.getRole());
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }
}