package frontend.game;

import main.accountService.AccountService;
import main.Context;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import resource.LoggerMessages;
import resource.ResourceFactory;


public class GameWebSocketCreator implements WebSocketCreator {

    final private Logger logger = LogManager.getLogger(GameWebSocketCreator.class.getName());
    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
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
        logger.info(loggerMessages.newSocket());
        if ((temp = accountService.getSessions(sessionId)) == null) {
            logger.info(loggerMessages.notAuthorised());
            return null;
        }
        return new GameWebSocket(temp.getLogin(), context);
    }
}
