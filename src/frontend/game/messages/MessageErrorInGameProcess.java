package frontend.game.messages;

import frontend.game.WebSocketService;
import mechanics.GameError;
import mechanics.GameUser;
import messageSystem.Address;
import utils.Id;

public final class MessageErrorInGameProcess extends MessageToSocketService{
    final private Id <GameUser> id;
    final private GameError gameError;

    public MessageErrorInGameProcess(Address from, Address to, Id<GameUser> id, GameError gameError) {
        super(from, to);
        this.id = id;
        this.gameError = gameError;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.notifyOpponentLostConnection(id, gameError);
    }
}
