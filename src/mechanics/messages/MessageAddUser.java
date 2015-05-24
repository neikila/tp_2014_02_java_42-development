package mechanics.messages;

import main.user.UserProfile;
import mechanics.GameMechanics;
import mechanics.GameUser;
import messageSystem.Address;
import utils.Id;

public final class MessageAddUser extends MessageToGameMechanics {
    final private Id <GameUser> id;
    final private UserProfile user;

    public MessageAddUser(Address from, Address to, Id<GameUser> id, UserProfile user) {
        super(from, to);
        this.id = id;
        this.user = user;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        gameMechanics.addUser(id, user);
    }
}
