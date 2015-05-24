package frontend.game;

import main.Context;
import main.accountService.AccountService;
import main.user.UserProfile;
import org.apache.logging.log4j.Logger;
import utils.LoggerMessages;
import utils.Messages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.PageGenerator.getPage;

public class GameServlet extends HttpServlet {

    final private Logger logger = getLogger(GameServlet.class.getName());
    final private AccountService accountService;

    public GameServlet(Context context) {
        this.accountService = (AccountService) context.get(AccountService.class);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        logger.info(LoggerMessages.doPostStart());

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile user = accountService.getSessions(request.getSession().getId());
        response.setStatus(SC_OK);
        response.setContentType("text/html;charset=utf-8");

        if (user != null) {
            logger.info(LoggerMessages.authorised(), user.getLogin());
            pageVariables.put("myName", user.getLogin());
            response.getWriter().println(getPage("game.html", pageVariables));
        } else {
            logger.info(LoggerMessages.notAuthorised());
            pageVariables.put("loginStatus", Messages.askToSignIn());
            response.getWriter().println(getPage("authstatus.html", pageVariables));
        }
        logger.info(LoggerMessages.doPostFinish());
    }
}