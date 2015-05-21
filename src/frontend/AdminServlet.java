package frontend;

import main.Context;
import main.accountService.AccountService;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import utils.Messages;
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

import static utils.LoggerMessages.*;

public class AdminServlet extends HttpServlet{

    final private Logger logger = LogManager.getLogger(AdminServlet.class.getName());
    final private AccountService accountService;

    public AdminServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        logger.info(doGetStart());
        logger.info(requestGetParams(), request.getParameterMap().toString());

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        if (user != null && user.isAdmin()) {
            logger.info(isAdmin(), user.getLogin());
            pageToReturn = "adminPage.html";
            pageVariables.put("titleMessage", Messages.adminPage());
        } else {
            if (user != null)
                logger.info(isNotAdmin(), user.getLogin());
            else
                logger.info(notAuthorised());
            pageVariables.put("errorMessage", Messages.notFound());
            pageToReturn = "errorPage.html";
        }
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
        logger.info(doGetFinish());
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        logger.info(doPostStart());
        logger.info(requestGetParams(), request.getParameterMap().toString());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(HttpServletResponse.SC_OK);

        String action = request.getParameter("action");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("errorMessage", Messages.notFound());
        String pageToReturn = "errorPage.html";

        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());
        if (user != null) {
            if (user.isAdmin()) {
                logger.info(isAdmin(), user.getLogin());
                if (action != null) {
                    switch (action) {
                        case "stop":
                            logger.info(stop());
                            StopServers();
                            break;
                        case "get":
                            logger.info(statistic());
                            JSONObject data = new JSONObject();
                            data.put("amountOfLoggedIn", accountService.getAmountOfSessions());
                            data.put("amountOfSignedUp", accountService.getAmountOfUsers());
                            logger.info(data.toString());
                            response.getWriter().write(data.toString());
                            break;
                        default:
                            pageVariables.put("errorMessage", Messages.wrongParamAction());
                            logger.warn(wrongAction());
                    }
                }
            } else {
                response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
                logger.info(isNotAdmin(), user.getLogin());
            }
        } else {
            response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
            logger.info(notAuthorised());
        }
        logger.info(doPostFinish());
    }

    protected void StopServers() {
        TimeHelper.sleep(100);
        System.exit(0);
    }
}