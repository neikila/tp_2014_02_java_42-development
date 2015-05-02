package frontend;

import main.accountService.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
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
        logger.info(loggerMessages.requestGetParams(), request.getParameterMap().toString());

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
        logger.info(loggerMessages.requestGetParams(), request.getParameterMap().toString());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(HttpServletResponse.SC_OK);

        String action = request.getParameter("action");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("errorMessage", messages.notFound());
        String pageToReturn = "errorPage.html";

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
                            JSONObject data = new JSONObject();
                            data.put("amountOfLoggedIn", accountService.getAmountOfSessions());
                            data.put("amountOfSignedUp", accountService.getAmountOfUsers());
                            logger.info(data.toString());
                            response.getWriter().write(data.toString());
                            break;
                        default:
                            pageVariables.put("errorMessage", messages.wrongParamAction());
                            logger.warn(loggerMessages.wrongAction());
                    }
                }
            } else {
                response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
                logger.info(loggerMessages.isNotAdmin(), user.getLogin());
            }
        } else {
            response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
            logger.info(loggerMessages.notAuthorised());
        }
        logger.info(loggerMessages.doPostFinish());
    }

    protected void StopServers() {
        TimeHelper.sleep(100);
        System.exit(0);
    }
}