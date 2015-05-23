package messageSystem;

import main.accountService.AccountService;
import mechanics.GameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AddressService {
    private Address gameMechanics;
    private List<Address> accountServiceList = new ArrayList<>();

    private AtomicInteger accountServiceCounter = new AtomicInteger();

//    public void registerFrontEnd(FrontEnd frontEnd) {
//        this.frontEnd = frontEnd.getAddress();
//    }

    public boolean registerGameMechanics(Abonent gameMechanics) {
        if (gameMechanics instanceof GameMechanics) {
            this.gameMechanics = gameMechanics.getAddress();
            return true;
        } else {
            return false;
        }
    }

    public void registerAccountService(AccountService accountService) {
            accountServiceList.add(accountService.getAddress());
    }

    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }

    public synchronized Address getAccountServiceAddress() {
        int index = accountServiceCounter.getAndIncrement();
        if (index >= accountServiceList.size()) {
            index = 0;
        }
        return accountServiceList.get(index);
    }
}
