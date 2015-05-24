package messageSystem;

import main.accountService.AccountServiceThread;
import mechanics.GameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AddressService {
    private List<Address> accountServiceList = new ArrayList<>();
    private List<Address> gameMechanicsList = new ArrayList<>();

    private AtomicInteger accountServiceCounter = new AtomicInteger();
    private AtomicInteger gameMechanicsCounter= new AtomicInteger();

//    public void registerFrontEnd(FrontEnd frontEnd) {
//        this.frontEnd = frontEnd.getAddress();
//    }

    public void registerGameMechanics(GameMechanics gameMechanics) {
        gameMechanicsList.add(gameMechanics.getAddress());
    }

    public void registerAccountService(AccountServiceThread accountServiceThread) {
        accountServiceList.add(accountServiceThread.getAddress());
    }

    // TODO два synchronized - плохо, можно ли поправить?
    public synchronized Address getGameMechanicsAddress() {
        int index = gameMechanicsCounter.getAndIncrement();
        if (index >= gameMechanicsList.size()) {
            index = 0;
        }
        return gameMechanicsList.get(index);
    }

    public synchronized Address getAccountServiceAddress() {
        int index = accountServiceCounter.getAndIncrement();
        if (index >= accountServiceList.size()) {
            index = 0;
        }
        return accountServiceList.get(index);
    }
}
