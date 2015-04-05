package main;

import Interface.AccountService;
import Interface.GameMechanics;
import Interface.WebSocketService;
import frontend.*;
import frontend.game.*;
import mechanics.GameMechanicsImpl;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.apache.logging.log4j.Logger;

public class AppServer {

    static final Logger logger = LogManager.getLogger(AppServer.class.getName());

    private GameMechanics gameMechanics;
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

        WebSocketService webSocketService = new WebSocketServiceImpl();
        contextGlobal.add(WebSocketService.class, webSocketService);
        gameMechanics = new GameMechanicsImpl(contextGlobal);
        contextGlobal.add(GameMechanics.class, gameMechanics);

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
            logger.info("Start");
            gameMechanics.run();
            //server.join();
        } catch (Exception e) {
            logger.fatal("There is an error in Server.Start()");
            System.exit(1);
        }
    }

    public void stop(){
        try {
            System.exit(0);
        } catch (Exception e) {
            logger.fatal("There is an error in Server.Stop()");
            System.exit(1);
        }
    }
}
