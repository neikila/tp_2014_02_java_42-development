package frontend.game;

import main.Context;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import utils.LoggerMessages;

import javax.servlet.annotation.WebServlet;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * This class represents a servlet starting a webSocket application
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/gameplay"})
public class WebSocketGameServlet extends WebSocketServlet {

    final private Logger logger = getLogger(WebSocketGameServlet.class.getName());

    final private static int IDLE_TIME = 60 * 1000;
    final private Context context;

    public WebSocketGameServlet(Context context) {
        this.context = context;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        logger.info(LoggerMessages.configure());
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(context));
    }
}
