package mechanics.messages;

import mechanics.GameMechanics;
import mechanics.GameUser;
import messageSystem.Address;
import utils.Id;

public final class MessageLostConnection extends MessageToGameMechanics {
    final private Id <GameUser> id;

    public MessageLostConnection(Address from, Address to, Id<GameUser> id) {
        super(from, to);
        this.id = id;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        gameMechanics.userLostConnection(id);
    }
}
