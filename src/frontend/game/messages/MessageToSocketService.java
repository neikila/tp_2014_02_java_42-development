package frontend.game.messages;

import frontend.game.WebSocketService;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

public abstract class MessageToSocketService extends Message {
    public MessageToSocketService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof WebSocketService) {
            exec((WebSocketService) abonent);
        }
    }

    protected abstract void exec(WebSocketService webSocketService);
}
