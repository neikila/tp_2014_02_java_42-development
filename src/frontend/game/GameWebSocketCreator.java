package frontend.game;

import main.Context;
import main.accountService.AccountService;
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
    final private AccountService accountService;

    public GameWebSocketCreator(Context context) {
        this.context = context;
        this.accountService = (AccountService) context.get(AccountService.class);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        UserProfile temp;
        logger.info(LoggerMessages.newSocket());
        if ((temp = accountService.getSessions(sessionId)) == null) {
            if ((temp = accountService.getUserFromPhoneSession(sessionId)) != null) {
                return new GameWebSocket(temp, context, true);
            } else {
                logger.info(LoggerMessages.notAuthorised());
            }
        } else {
            return new GameWebSocket(temp, context, false);
        }

        logger.info("Huston we have some problems");
        //  TODO
        return null;
    }
}
