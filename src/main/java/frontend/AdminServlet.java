package frontend;

import main.AccountService;
import main.AppServer;
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

public class AdminServlet extends HttpServlet {

    private AccountService accountService;

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
                    case "Stop server":
                        pageVariables.put("topicMessage", "Statistic");
                        pageToReturn = "byeBye.html";
                        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
                        System.exit(0);
                        break;
                    case "Get statistic":
                        pageVariables.put("topicMessage", "Statistic");
                        pageVariables.put("amountOfLoggedIn", accountService.getAmountOfSessions());
                        pageVariables.put("amountOfSignedUp", accountService.getAmountOfUsers());
                        pageToReturn = "statistic.html";
                        break;
                }
            }
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }
}