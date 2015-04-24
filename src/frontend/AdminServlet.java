package frontend;

import Interface.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.LoggerMessages;
import resource.Messages;
import resource.ResourceFactory;
import utils.PageGenerator;
import utils.TimeHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet{

    final private Logger logger = LogManager.getLogger(AdminServlet.class.getName());
    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    final private Messages messages = (Messages) ResourceFactory.instance().getResource("messages");
    final private AccountService accountService;

    public AdminServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        logger.info(loggerMessages.doGetStart());

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        if (user != null && user.isAdmin()) {
            logger.info(loggerMessages.isAdmin(), user.getLogin());
            pageToReturn = "adminPage.html";
            pageVariables.put("titleMessage", messages.adminPage());
        } else {
            if (user != null)
                logger.info(loggerMessages.isNotAdmin(), user.getLogin());
            else
                logger.info(loggerMessages.notAuthorised());
            pageVariables.put("errorMessage", messages.notFound());
            pageToReturn = "errorPage.html";
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        logger.info(loggerMessages.doGetFinish());
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        logger.info(loggerMessages.doPostStart());
        String action = request.getParameter("action");

        String pageToReturn;
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        pageVariables.put("errorMessage", messages.notFound());
        pageToReturn = "errorPage.html";

        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());
        if (user != null) {
            if (user.isAdmin()) {
                logger.info(loggerMessages.isAdmin(), user.getLogin());
                if (action != null) {
                    switch (action) {
                        case "stop":
                            logger.info(loggerMessages.stop());
                            StopServers();
                            break;
                        case "get":
                            logger.info(loggerMessages.statistic());
                            // TODO переделать в json
                            response.setStatus(HttpServletResponse.SC_OK);
                            pageVariables.put("topicMessage", "Statistic");
                            pageVariables.put("amountOfLoggedIn", accountService.getAmountOfSessions());
                            pageVariables.put("amountOfSignedUp", accountService.getAmountOfUsers());
                            pageToReturn = "statistic.html";
                            break;
                        default:
                            pageVariables.put("errorMessage", messages.wrongParamAction());
                            pageToReturn = "errorPage.html";
                            logger.warn(loggerMessages.wrongAction());
                    }
                }
            } else {
                logger.info(loggerMessages.isNotAdmin(), user.getLogin());
            }
        } else {
            logger.info(loggerMessages.notAuthorised());
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        logger.info(loggerMessages.doPostFinish());
    }

    protected void StopServers() {
        TimeHelper.sleep(100);
        System.exit(0);
    }
}