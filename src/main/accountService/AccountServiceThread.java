package main.accountService;

import main.Context;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import resource.ResourceFactory;
import resource.ThreadsSettings;

public final class AccountServiceThread implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final int stepTime;

    private final AccountService accountService;

    public AccountServiceThread(Context context) {
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);

        this.accountService = (AccountService) context.get(AccountService.class);
        stepTime = ((ThreadsSettings) ResourceFactory.instance().getResource("threadsSettings")).getASTimeStep();
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
                Thread.sleep(stepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
