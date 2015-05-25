package frontend.game;

import main.Context;
import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.json.simple.JSONObject;
import utils.Id;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private Map<Id <GameUser>, GameWebSocket> userSockets = new HashMap<>();

    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final short STEP_TIME = 10;

    public WebSocketServiceImpl(Context context) {
        messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerSocketService(this);
    }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void addUser(GameWebSocket user) {
        userSockets.put(user.getId(), user);
    }

    @Override
    public void sendSettings(Id <GameUser> id, GameMap map) {
        userSockets.get(id).settings(map);
    }

    @Override
    public void notifyAction(Id <GameUser> id, JSONObject action) {
        GameWebSocket gameWebSocket = userSockets.get(id);
        gameWebSocket.sendAction(action);
    }

    @Override
    public void sync(GameSession session, Id <GameUser> id) {
        GameWebSocket gameWebSocket = userSockets.get(id);
        gameWebSocket.sync(session, id);
    }
    @Override
    public void notifyStartGame(GameSession session, Id <GameUser> id) {
        GameWebSocket gameWebSocket = userSockets.get(id);
        gameWebSocket.startGame(session, id);
    }

    @Override
    public void notifyGameOver(Id <GameUser> id, int result) {
        userSockets.get(id).gameOver(result);
    }
}