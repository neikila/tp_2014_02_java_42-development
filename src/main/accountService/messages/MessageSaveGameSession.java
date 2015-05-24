package main.accountService.messages;

import main.accountService.AccountServiceThread;
import mechanics.GameSession;
import mechanics.messages.MessageGameSessionSaveReply;
import messageSystem.Address;
import messageSystem.Message;

public final class MessageSaveGameSession extends MessageToAccountService {
    final private GameSession gameSession;

    public MessageSaveGameSession(Address from, Address to, GameSession gameSession) {
        super(from, to);
        this.gameSession = gameSession;
    }

    @Override
    protected void exec(AccountServiceThread service) {
        service.getAccountService().updateUser(gameSession.getFirst().getUser());
        service.getAccountService().updateUser(gameSession.getSecond().getUser());
        final Message back = new MessageGameSessionSaveReply(getTo(), getFrom(), gameSession);
        service.getMessageSystem().sendMessage(back);
    }
}
