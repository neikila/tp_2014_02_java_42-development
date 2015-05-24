package messageSystem;

import main.accountService.AccountServiceThread;
import mechanics.GameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AddressService {
    private List<Address> accountServiceList = new ArrayList<>();
    private AtomicInteger accountServiceCounter = new AtomicInteger();
    final private Object syncObj = new Object();

    private Address gameMechanics;

    public void registerGameMechanics(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public void registerAccountService(AccountServiceThread accountServiceThread) {
        accountServiceList.add(accountServiceThread.getAddress());
    }

    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }

    public Address getAccountServiceAddress() {
        int index;
        synchronized (syncObj) {
            index = accountServiceCounter.getAndIncrement();
            if (index >= accountServiceList.size()) {
                accountServiceCounter.set(1);
                index = 0;
            }
        }
        return accountServiceList.get(index);
    }
}
