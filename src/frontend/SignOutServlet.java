package frontend;

import Interface.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import resource.LoggerMessages;
import resource.Messages;
import resource.ResourceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignOutServlet extends HttpServlet{

    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    final private Logger logger = LogManager.getLogger(SignOutServlet.class.getName());
    final private Messages messages = (Messages) ResourceFactory.instance().getResource("messages");
    final private AccountService accountService;

    public SignOutServlet(Context contextGlobal) {
        this.accountService = (AccountService)contextGlobal.get(AccountService.class);
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        logger.info(loggerMessages.doPostStart());
        HttpSession session = request.getSession();

        UserProfile user = accountService.getSessions(session.getId());

        short status;

        if (user != null) {
            logger.info(loggerMessages.loggedOut(), user.getLogin());
            accountService.removeSession(session.getId());
            status = 200;
        } else {
            logger.info(loggerMessages.notAuthorised());
            status = 401;
        }
        createResponse(response, status);
        logger.info(loggerMessages.doPostFinish());
    }


    @SuppressWarnings("unchecked")
    private void createResponse(HttpServletResponse response, short status) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(HttpServletResponse.SC_OK);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        obj.put("data", data);
        if (status != 200) {
            data.put("message", messages.notAuthorised());
        }
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}