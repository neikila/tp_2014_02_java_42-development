package frontend.game.messages;

import frontend.game.WebSocketService;
import mechanics.GameMap;
import mechanics.GameUser;
import messageSystem.Address;
import utils.Id;

public final class MessageSendSettings extends MessageToSocketService {
    final private Id <GameUser> id;
    final private GameMap map;

    public MessageSendSettings(Address from, Address to, Id<GameUser> id, GameMap map) {
        super(from, to);
        this.id = id;
        this.map = map;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.sendSettings(id, map);
    }
}
