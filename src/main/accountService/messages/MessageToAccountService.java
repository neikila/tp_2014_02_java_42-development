package main.accountService.messages;

import main.accountService.AccountServiceThread;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof AccountServiceThread) {
            exec((AccountServiceThread) abonent);
        }
    }

    protected abstract void exec(AccountServiceThread service);
}
