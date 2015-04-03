package frontend.game;

import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import resource.LoggerMessages;
import resource.ResourceFactory;


public class GameWebSocketCreator implements WebSocketCreator {

    final private Context context;

    public GameWebSocketCreator(Context context) {
        this.context = context;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        return new GameWebSocket(sessionId, context);
    }
}
