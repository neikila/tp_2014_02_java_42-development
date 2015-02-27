package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class ProfileServlet extends HttpServlet {

    private AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        // Задание строки с названием файла, на основе которого будет генерироваться ответ
        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        // Создание объекта сессии, полученной от полльзователя в запросе
        HttpSession session = request.getSession();

        // Идентификация пользователя на основе id-хэша
        UserProfile user = accountService.getSessions(session.getId());

        // Если пользователь не определен, то возвращаем ему странницу авторизации
        // в противном случае достаем "из базы" информацию о пользователе
        if (user == null)
        {
            pageVariables.put("loginStatus", "You haven't Logged In:");
            pageToReturn = "signInForm.html";
        } else {
            pageToReturn = "profile.html";
            pageVariables.put("login", user.getLogin());
            pageVariables.put("password", user.getPassword());
            pageVariables.put("email", user.getEmail());
            pageVariables.put("server", user.getServer());
            pageVariables.put("role", user.getRole());
        }

        // Генерирование страницы с заданными параметрами.
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
    }
}
