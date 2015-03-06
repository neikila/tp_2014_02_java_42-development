package frontend;

import main.AccountService;
import main.UserProfile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScoreServlet extends HttpServlet {

    private AccountService accountService;

    public ScoreServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        short status;

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        String message = "mysql error";
        status = 200; // Без базы ошибки нет

        String username[] =  new String [4];
        username[0] = "Vasya";
        username[1] = "Vanya";
        username[2] = "Petya";
        username[3] = "Danya";

        int scoreMas[] = {14, 12, 10, 2};

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray scoreList = new JSONArray();
        JSONObject scoreItem;

        for (int i = 0; i < 4; ++i) {
            scoreItem = new JSONObject();
            scoreItem.put("login", username[i]);
            scoreItem.put("score", scoreMas[i]);
            scoreList.add(scoreItem);
        }

        if (status != 200) {
            data.put("message", message);
        } else {
            data.put("scoreList", scoreList);
        }
        obj.put("data", data);
        obj.put("status", status);
        response.getWriter().write(obj.toString());
    }
}