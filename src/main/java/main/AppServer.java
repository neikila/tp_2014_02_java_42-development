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

    public AppServer(int port, AccountService accountService) {

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //TODO создание url

        addServletToContext(context, new SignInServlet(accountService), "/auth/signin");
        addServletToContext(context, new SignUpServlet(accountService), "/auth/signup");
        addServletToContext(context, new ProfileServlet(accountService), "/auth/check");
        addServletToContext(context, new AdminServlet(accountService), "/auth/admin");
        addServletToContext(context, new SignOutServlet(accountService), "/auth/signout");
        addServletToContext(context, new ScoreServlet(accountService), "/score");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
    }

    private void addServletToContext(ServletContextHandler context, Servlet servlet, String entity){
        String apiVersion = "v1";
        String url = "/api/" + apiVersion + entity;
        context.addServlet(new ServletHolder(servlet), url);
    }

    public void start(){
        try {
            server.start();
            server.join();
        } catch (Exception e) { System.out.append("There is an error in Server.Start()"); System.exit(1); }
    }

    public void stop(){
        try {
            System.exit(0);
        } catch (Exception e) { System.out.append("There is an error in Server.Stop()"); System.exit(1); }
    }
}
