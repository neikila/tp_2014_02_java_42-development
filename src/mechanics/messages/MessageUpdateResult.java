package mechanics.messages;

import mechanics.GameMechanics;
import messageSystem.Address;

public final class MessageUpdateResult extends MessageToGameMechanics {
    private boolean result;

    public MessageUpdateResult(Address from, Address to, boolean result) {
        super(from, to);
        this.result = result;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        // TODO заглушечка
        if (result)
            System.out.println("Success in updating User from gameMechanic");
        else
            System.out.println("Error while updating User from gameMechanic");
    }
}
