package messageSystem;

import frontend.game.WebSocketService;
import main.accountService.AccountServiceThread;
import mechanics.GameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AddressService {
    private List<Address> accountServiceList = new ArrayList<>();
    private AtomicInteger accountServiceCounter = new AtomicInteger();
    final private Object syncObjAS = new Object();

    private List<Address> gameMechanicsList = new ArrayList<>();
    private AtomicInteger gameMechanicsCounter = new AtomicInteger();
    final private Object syncObjGM = new Object();

    private Address webSocketService;

    public void registerSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService.getAddress();
    }

    public void registerGameMechanics(GameMechanics gameMechanics) {
        gameMechanicsList.add(gameMechanics.getAddress());
    }

    public void registerAccountService(AccountServiceThread accountServiceThread) {
        accountServiceList.add(accountServiceThread.getAddress());
    }

    public Address getWebSocketServiceAddress() {
        return webSocketService;
    }

    public Address getGameMechanicsAddress() {
        int index;
        synchronized (syncObjGM) {
            index = gameMechanicsCounter.getAndIncrement();
            if (index >= 2 * gameMechanicsList.size()) {
                gameMechanicsCounter.set(1);
                index = 0;
            }
        }
        return gameMechanicsList.get(index / 2);
    }

    public Address getAccountServiceAddress() {
        int index;
        synchronized (syncObjAS) {
            index = accountServiceCounter.getAndIncrement();
            if (index >= accountServiceList.size()) {
                accountServiceCounter.set(1);
                index = 0;
            }
        }
        return accountServiceList.get(index);
    }
}
