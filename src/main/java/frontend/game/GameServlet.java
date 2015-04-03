package frontend.game;

import Interface.AccountService;
import main.UserProfile;
import utils.TimeHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServlet extends HttpServlet {

    private AccountService accountService;

    public GameServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile user = accountService.getSessions(request.getSession().getId());
        if(user != null) {
            String name = user.getLogin();
            pageVariables.put("myName", name);

            response.getWriter().println(utils.PageGenerator.getPage("game.html", pageVariables));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            TimeHelper.sleep(1000);
        } else {
            // TODO переделать на нормальное сообщение об отсутсвие в базе
            super.doPost(request, response);
        }
    }
}