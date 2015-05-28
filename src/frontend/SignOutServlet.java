package frontend;

import main.Context;
import main.accountService.AccountService;
import main.user.UserProfile;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import utils.LoggerMessages;
import utils.Messages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.logging.log4j.LogManager.getLogger;

public class SignOutServlet extends HttpServlet {

    final private Logger logger = getLogger(SignOutServlet.class.getName());
    final private AccountService accountService;

    public SignOutServlet(Context contextGlobal) {
        this.accountService = (AccountService) contextGlobal.get(AccountService.class);
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        logger.info(LoggerMessages.doPostStart());
        logger.info(LoggerMessages.requestGetParams(), request.getParameterMap().toString());
        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());

        short status;

        if (user != null) {
            logger.info(LoggerMessages.loggedOut(), user.getLogin());
            accountService.removeSession(session.getId());
            accountService.removePhoneSession(user.getLogin());
            status = 200;
        } else {
            logger.info(LoggerMessages.notAuthorised());
            status = 401;
        }
        createResponse(response, status);
        logger.info(LoggerMessages.doPostFinish());
    }


    @SuppressWarnings("unchecked")
    private void createResponse(HttpServletResponse response, short status) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(SC_OK);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        obj.put("data", data);
        if (status != 200) {
            data.put("message", Messages.notAuthorised());
        }
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}