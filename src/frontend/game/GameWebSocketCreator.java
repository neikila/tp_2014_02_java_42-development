package frontend.game;

import main.Context;
import main.accountService.AccountService;
import main.user.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import utils.Id;
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

        if ((temp = accountService.getUserFromPhoneSession(sessionId)) != null) {
            logger.info("New phoneSocket for user {} with id {}", temp.getLogin(), temp.getId());
            return new PhoneWebSocket(new Id<>(temp.getId()), context);
        }

        if ((temp = accountService.getSessions(sessionId)) != null) {
            logger.info("New webSocket for user {} with id {}", temp.getLogin(), temp.getId());
            return new GameWebSocket(temp, context);
        }

        logger.info(LoggerMessages.notAuthorised());
        return null;
    }
}
