package frontend.game.messages;

import frontend.game.WebSocketService;
import mechanics.GameSession;
import mechanics.GameUser;
import messageSystem.Address;
import utils.Id;

public final class MessageStartGame extends MessageToSocketService{
    final private GameSession gameSession;
    final private Id <GameUser> id;

    public MessageStartGame(Address from, Address to, GameSession gameSession, Id<GameUser> id) {
        super(from, to);
        this.id = id;
        this.gameSession = gameSession;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.notifyStartGame(gameSession, id);
    }
}
