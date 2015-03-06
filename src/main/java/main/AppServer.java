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

        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet profileInfo = new ProfileServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet admin = new AdminServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(admin), "/api/v1/auth/admin");
        context.addServlet(new ServletHolder(signIn), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(profileInfo), "/api/v1/auth/check");
        context.addServlet(new ServletHolder(signOut), "/api/v1/auth/signout");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
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
