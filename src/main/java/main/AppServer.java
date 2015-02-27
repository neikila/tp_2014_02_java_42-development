package main;

import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class AppServer {
    private Server server;
    private Servlet signIn;
    private Servlet signUp;
    private Servlet profileInfo;
    private Servlet logOut;
    private Servlet admin;
    private ServletContextHandler context;
    private int port;
    private ResourceHandler resource_handler;
    private HandlerList handlers;

    public AppServer(int port, AccountService accountService) {

        this.port = port;
        server = new Server(port);

        // Создание объекта сервлета, который бдет обрабатывать запрос
        signIn = new SignInServlet(accountService);
        signUp = new SignUpServlet(accountService);
        profileInfo = new ProfileServlet(accountService);
        logOut = new LogOutServlet(accountService);
        admin = new AdminServlet(accountService, this);

        // Распределение: какой сервлет соответствует какому url
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(admin), "/api/v1/auth/admin");
        context.addServlet(new ServletHolder(signIn), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(profileInfo), "/api/v1/auth/profile");
        context.addServlet(new ServletHolder(logOut), "/api/v1/auth/logout");

        resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);
    }

    public void Start(){
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void Stop(){
        try {
            server.stop();
            server.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
