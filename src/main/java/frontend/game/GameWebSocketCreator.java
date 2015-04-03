package frontend.game;

import Interface.AccountService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;


public class GameWebSocketCreator implements WebSocketCreator {
    private AccountService accountService;
    private base.GameMechanics gameMechanics;
    private base.WebSocketService webSocketService;

    public GameWebSocketCreator(AccountService accountService,
                                base.GameMechanics gameMechanics,
                                base.WebSocketService webSocketService) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        String name = accountService.getSessions(sessionId).getLogin();
        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
