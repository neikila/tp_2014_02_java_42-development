package frontend.game.messages;

import frontend.game.WebSocketService;
import mechanics.GameUser;
import messageSystem.Address;
import utils.Id;

public final class MessageGameOver extends MessageToSocketService{
    final private Id <GameUser> id;
    final private int gameResult;

    public MessageGameOver(Address from, Address to, Id<GameUser> id, int gameResult) {
        super(from, to);
        this.id = id;
        this.gameResult = gameResult;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.notifyGameOver(id, gameResult);
    }
}
