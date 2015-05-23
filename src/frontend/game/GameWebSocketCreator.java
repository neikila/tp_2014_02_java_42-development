package frontend.game;

import main.Context;
import main.accountService.AccountServiceDAO;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import utils.LoggerMessages;


public class GameWebSocketCreator implements WebSocketCreator {

    final private Logger logger = LogManager.getLogger(GameWebSocketCreator.class.getName());
    final private Context context;
    final private AccountServiceDAO accountServiceDAO;

    public GameWebSocketCreator(Context context) {
        this.context = context;
        this.accountServiceDAO = (AccountServiceDAO) context.get(AccountServiceDAO.class);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        UserProfile temp;
        logger.info(LoggerMessages.newSocket());
        if ((temp = accountServiceDAO.getSessions(sessionId)) == null) {
            logger.info(LoggerMessages.notAuthorised());
            return null;
        }
        return new GameWebSocket(temp, context);
    }
}
