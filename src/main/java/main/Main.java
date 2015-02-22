package main;

import frontend.LogOutServlet;
import frontend.ProfileServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
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
    private static void preparationForTest(AccountService accountService) {
        // Создание в базе пользователя по дефолту. Имитация бд ввиду её отстсвия.
        String login = "admin";
        String password = "admin";
        String server = "10";
        String email = "admin@gmail.com";
        accountService.addUser("admin",  new UserProfile(login, password, email, server));
        accountService.addSessions("1", new UserProfile(login, password, email, server));
    }
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AccountService accountService = new AccountService();

        preparationForTest(accountService);

        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet profileInfo = new ProfileServlet(accountService);
        Servlet logOut = new LogOutServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
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