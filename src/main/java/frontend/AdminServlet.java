package frontend;

import main.AccountService;
import main.UserProfile;
import org.eclipse.jetty.server.Server;
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
 * @author v.chibrikov
 */
public class AdminServlet extends HttpServlet {

    private AccountService accountService;
    private Server server;

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile profile = accountService.getSessions(session.getId());

        if (profile != null && profile.isAdmin()) {
            pageToReturn = "adminPage.html";
            pageVariables.put("titleMessage", "Admin page");
        } else {
            pageVariables.put("errorMessage", "404: Not Found.");
            pageToReturn = "errorPage.html";
        }

        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.append("Action = ").append(action).append(' ').append((action == null)?"true\n":"false\n");

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("errorMessage", "404: Not Found.");
        pageToReturn = "errorPage.html";

        HttpSession session = request.getSession();

        UserProfile profile = accountService.getSessions(session.getId());
        if (profile != null && profile.isAdmin()) {
            if(action != null)
            {
                switch (action) {
                    case ("Stop server"):
                        try {

                            System.out.append("Success");
                        }  catch (Exception e) {
                            System.out.append("Not stopped");
                            e.printStackTrace();
                        }
                        break;
                    case ("Get Statistic"):
                        break;
                }
            }
        }
        // В случае, если пользователь подтверждает logout, удаляем пользователя из сессии и возвращаем страницу авторизации
/*        if (answer.equals("Yes") || profile == null) {
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
        }*/
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }
}