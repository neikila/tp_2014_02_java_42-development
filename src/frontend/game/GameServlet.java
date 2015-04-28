package frontend.game;

import Interface.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.LoggerMessages;
import resource.Messages;
import resource.ResourceFactory;
import utils.PageGenerator;

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
    final private Messages messages = (Messages) ResourceFactory.instance().getResource("messages");
    final private AccountService accountService;

    public GameServlet(Context context) {
        this.accountService = (AccountService) context.get(AccountService.class);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        logger.info(loggerMessages.doPostStart());

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile user = accountService.getSessions(request.getSession().getId());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");

        if(user != null) {
            logger.info(loggerMessages.authorised(), user.getLogin());
            pageVariables.put("myName", user.getLogin());
            response.getWriter().println(utils.PageGenerator.getPage("game.html", pageVariables));
        } else {
            logger.info(loggerMessages.notAuthorised());
            pageVariables.put("loginStatus", messages.askToSignIn());
            response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
        }
        logger.info(loggerMessages.doPostFinish());
    }
}