package main;

import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {

    // Действия необходимые для тестирования
    // Вызывается в начале запуска сервера
    private static void preparationForTest(AccountService accountService) {
        // Создание в базе пользователя по дефолту. Имитация бд ввиду её отстсвия.
        String login = "admin";
        String password = "admin";
        String server = "10";
        String email = "admin@gmail.com";
        UserProfile profile = new UserProfile(login, password, email, server);
        profile.setRole("Admin");
        accountService.addUser("admin",  profile);
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        AccountService accountService = new AccountService();
        preparationForTest(accountService);

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');



        // Создание объекта сервлета, который бдет обрабатывать запрос
        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet profileInfo = new ProfileServlet(accountService);
        Servlet logOut = new LogOutServlet(accountService);
        Servlet admin = new AdminServlet(accountService);

        // Распределение: какой сервлет соответствует какому url
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(admin), "/api/v1/auth/admin");
        context.addServlet(new ServletHolder(signIn), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(profileInfo), "/api/v1/auth/profile");
        context.addServlet(new ServletHolder(logOut), "/api/v1/auth/logout");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();

    }
}