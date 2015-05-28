package main;

import frontend.*;
import frontend.game.GameServlet;
import frontend.game.WebSocketGameServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class AppServer {

    static final Logger logger = LogManager.getLogger(AppServer.class.getName());

    private Server server;

    public AppServer(Context contextGlobal, int port) {

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        String  apiVersion = "v1";
        String url = "/api/" + apiVersion;

        context.addServlet(new ServletHolder(new SignInServlet(contextGlobal)), url + "/auth/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(contextGlobal)), url + "/auth/signup");
        context.addServlet(new ServletHolder(new ProfileServlet(contextGlobal)), url + "/auth/check");
        context.addServlet(new ServletHolder(new AdminServlet(contextGlobal)), url + "/auth/admin");
        context.addServlet(new ServletHolder(new SignOutServlet(contextGlobal)), url + "/auth/signout");
        context.addServlet(new ServletHolder(new ScoreServlet(contextGlobal)), url + "/score");
        context.addServlet(new ServletHolder(new PhoneServlet(contextGlobal)), url + "/auth/gamepad");

        context.addServlet(new ServletHolder(new WebSocketGameServlet(contextGlobal)), "/gameplay");
        context.addServlet(new ServletHolder(new GameServlet(contextGlobal)), "/game.html");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
    }

    public void start(){
        try {
            server.start();
        } catch (Exception e) {
            logger.fatal("There is an error in Server.Start()");
            logger.fatal(e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        try {
            System.exit(0);
        } catch (Exception e) {
            logger.fatal("There is an error in Server.Stop()");
            logger.fatal(e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Server getServer() {
        return server;
    }
}
