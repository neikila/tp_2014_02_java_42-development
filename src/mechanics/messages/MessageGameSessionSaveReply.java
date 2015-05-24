package mechanics.messages;

import mechanics.GameMechanics;
import mechanics.GameSession;
import messageSystem.Address;

public final class MessageGameSessionSaveReply extends MessageToGameMechanics {
    private GameSession session;

    public MessageGameSessionSaveReply(Address from, Address to, GameSession session) {
        super(from, to);
        this.session = session;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        gameMechanics.removeSession(session);
    }
}
