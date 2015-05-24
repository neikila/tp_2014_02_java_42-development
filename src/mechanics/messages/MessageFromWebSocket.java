package mechanics.messages;

import mechanics.GameMechanics;
import mechanics.GameUser;
import messageSystem.Address;
import org.json.simple.JSONObject;
import utils.Id;

public final class MessageFromWebSocket extends MessageToGameMechanics {
    final private Id <GameUser> id;
    final private JSONObject jsonObject;

    public MessageFromWebSocket(Address from, Address to, Id<GameUser> id, JSONObject jsonObject) {
        super(from, to);
        this.id = id;
        this.jsonObject = jsonObject;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        gameMechanics.analyzeMessage(id, jsonObject);
    }
}
