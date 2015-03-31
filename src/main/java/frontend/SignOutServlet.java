package frontend;

import Interface.AccountService;
import Interface.FrontendServlet;
import main.UserProfile;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignOutServlet extends HttpServlet{

    private AccountService accountService;

    public SignOutServlet(AccountService accountService) {
        this.accountService = accountService;
    }


    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        UserProfile profile = accountService.getSessions(session.getId());

        short status;

        if (profile != null) {
            accountService.removeSession(session.getId());
            status = 200;
        } else {
            status = 401;
        }
        createResponse(response, status);
    }


    private void createResponse(HttpServletResponse response, short status) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        if (status != 200) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Unauthorized");
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}