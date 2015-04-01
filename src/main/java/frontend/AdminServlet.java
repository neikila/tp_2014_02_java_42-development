package frontend;

import Interface.AccountService;
import main.Context;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet{

    static final private Logger logger = LogManager.getLogger(AdminServlet.class.getName());
    private AccountService accountService;

    public AdminServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        logger.info("doGet Start");

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        if (user != null && user.isAdmin()) {
            logger.info("User:" + user.getLogin() + " is admin");
            pageToReturn = "adminPage.html";
            pageVariables.put("titleMessage", "Admin page");
        } else {
            logger.info("User:" + user.getLogin() + " is not admin");
            pageVariables.put("errorMessage", "404: Not Found.");
            pageToReturn = "errorPage.html";
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        logger.info("doGet Success");
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost Start");
        String action = request.getParameter("action");

        String pageToReturn;
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        pageVariables.put("errorMessage", "404: Not Found.");
        pageToReturn = "errorPage.html";

        HttpSession session = request.getSession();

        UserProfile profile = accountService.getSessions(session.getId());
        if (profile != null && profile.isAdmin()) {
            logger.info("User: {} is Admin", profile.getLogin());
            if(action != null)
            {
                switch (action) {
                    case "stop":
                        logger.info("Stopping server");
                        /*pageVariables.put("topicMessage", "Statistic");
                        pageToReturn = "byeBye.html";
                        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));*/
                        StopServers();
                        break;
                    case "get":
                        logger.info("Getting Statistic");
                        response.setStatus(HttpServletResponse.SC_OK);
                        pageVariables.put("topicMessage", "Statistic");
                        pageVariables.put("amountOfLoggedIn", accountService.getAmountOfSessions());
                        pageVariables.put("amountOfSignedUp", accountService.getAmountOfUsers());
                        pageToReturn = "statistic.html";
                        break;
                    default:
                        logger.warn("Wrong action");
                }
            }
        } else {
            logger.info("User is not admin");
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        logger.info("doPost Success");
    }

    protected void StopServers() {
        //TODO some logic here: logger for example
        System.exit(0);
    }
}