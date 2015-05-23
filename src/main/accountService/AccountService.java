package main.accountService;

import main.Context;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

public final class AccountService implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final AccountServiceDAO accountServiceDAO;

    public AccountService(Context context) {
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);

        this.accountServiceDAO = (AccountServiceDAO) context.get(AccountServiceDAO.class);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public AccountServiceDAO getAccountServiceDAO() {
        return accountServiceDAO;
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
