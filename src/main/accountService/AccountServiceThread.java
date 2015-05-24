package main.accountService;

import main.Context;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

public final class AccountServiceThread implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final AccountService accountService;

    public AccountServiceThread(Context context) {
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);

        this.accountService = (AccountService) context.get(AccountService.class);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
