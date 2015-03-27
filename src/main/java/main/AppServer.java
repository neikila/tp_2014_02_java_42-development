package main;

import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class AppServer {
    private Server server;

    public AppServer(int port, AccountService accountService) {

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        String  apiVersion = "v1";
        String url = "/api/" + apiVersion;

        context.addServlet(new ServletHolder(new SignInServlet(accountService)), url + "/auth/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), url + "/auth/signup");
        context.addServlet(new ServletHolder(new ProfileServlet(accountService)), url + "/auth/check");
        context.addServlet(new ServletHolder(new AdminServlet(accountService)), url + "/auth/admin");
        context.addServlet(new ServletHolder(new SignOutServlet(accountService)), url + "/auth/signout");
        context.addServlet(new ServletHolder(new ScoreServlet(accountService)), url + "/score");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

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
