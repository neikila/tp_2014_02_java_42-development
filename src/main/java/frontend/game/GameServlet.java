package frontend.game;

import Interface.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.LoggerMessages;
import resource.ResourceFactory;
import utils.TimeHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServlet extends HttpServlet {

    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    final private Logger logger = LogManager.getLogger(GameServlet.class.getName());
    final private AccountService accountService;

    public GameServlet(Context context) {
        this.accountService = (AccountService) context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        logger.info(loggerMessages.doPostStart());

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile user = accountService.getSessions(request.getSession().getId());
        if(user != null) {
            logger.info(loggerMessages.authorised(), user.getLogin());
            String name = user.getLogin();
            pageVariables.put("myName", name);

            response.getWriter().println(utils.PageGenerator.getPage("game.html", pageVariables));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            //TimeHelper.sleep(1000);
        } else {
            logger.info(loggerMessages.notAuthorised());
            // TODO переделать на нормальное сообщение об отсутсвие в базе
            super.doPost(request, response);
        }
        logger.info(loggerMessages.doPostFinish());
    }
}