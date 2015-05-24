package frontend.game.messages;

import frontend.game.WebSocketService;
import mechanics.GameUser;
import messageSystem.Address;
import org.json.simple.JSONObject;
import utils.Id;

public final class MessageAction extends MessageToSocketService {
    final private Id <GameUser> id;
    final private JSONObject jsonObject;

    public MessageAction(Address from, Address to, Id<GameUser> id, JSONObject jsonObject) {
        super(from, to);
        this.id = id;
        this.jsonObject = jsonObject;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.notifyAction(id, jsonObject);
    }
}
