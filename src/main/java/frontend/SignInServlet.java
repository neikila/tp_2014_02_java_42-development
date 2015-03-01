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

public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn;

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserProfile user = accountService.getSessions(session.getId());

        String loginStatus;

        if (user == null) {
            loginStatus = "Log In:";
            pageToReturn = "signInForm.html";
        } else {
            loginStatus = "You have already logged in";
            pageToReturn = "authstatus.html";
        }

        pageVariables.put("loginStatus", loginStatus);

        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        String pageToReturn = "authstatus.html";

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();

        String message;

        // Проверка авторизован ли уже польлзвоатель
        if (!accountService.isSessionWithSuchLoginExist(login)) {
            // Получение профиля польлзователя по username
            UserProfile profile = accountService.getUser(login);
            // Если профиль найден, то проверка пароля
            if (profile != null && profile.getPassword().equals(password)) {
                accountService.addSessions(session.getId(), profile);
                // Задание сообщения об успешности операции
                message = "Login passed";
            } else {
                // Замена результирующей страницы на страницу повторного воода с соответствующим сообщением
                message = "Wrong login/password! Try again.";
                pageToReturn = "signInForm.html";
            }
        } else {
            // Создание сообщения о следующей ошибке: пользователь с заданным логином уже авторизован
            // и замена результирующей страницы на страницу авторизации
            message = "User with such login is already online.";
            pageToReturn = "signInForm.html";
        }
        //Запись сообщения в переменные, передаваемые в генератор страницы
        pageVariables.put("loginStatus", message);

        //Генерация странницы
        response.getWriter().println(PageGenerator.getPage(pageToReturn, pageVariables));
    }
}
