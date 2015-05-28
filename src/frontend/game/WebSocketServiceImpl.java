package frontend.game;

import main.Context;
import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import utils.Id;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    final private Logger logger = LogManager.getLogger(WebSocketService.class);
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
    public GameWebSocket getSocket(Id<GameUser> id) {
        return userSockets.get(id);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void addUser(GameWebSocket user) {
        userSockets.put(user.getId(), user);
    }

    // TODO переделать на лямбды
    @Override
    public void sendSettings(Id <GameUser> id, GameMap map) {
       GameWebSocket socket;
        if ((socket = userSockets.get(id)) != null) {
            socket.settings(map);
        } else {
            logger.info("No logger related to such GameUser ID: {}", id);
        }
    }

    @Override
    public void notifyAction(Id <GameUser> id, JSONObject action) {
        GameWebSocket socket;
        if ((socket = userSockets.get(id)) != null) {
            socket.sendAction(action);
        } else {
            logger.info("No logger related to such GameUser ID: {}", id);
        }
    }

    @Override
    public void sync(GameSession session, Id <GameUser> id) {
        GameWebSocket socket;
        if ((socket = userSockets.get(id)) != null) {
            socket.sync(session, id);
        } else {
            logger.info("No logger related to such GameUser ID: {}", id);
        }
    }

    @Override
    public void notifyStartGame(GameSession session, Id <GameUser> id) {
        GameWebSocket socket;
        if ((socket = userSockets.get(id)) != null) {
            socket.startGame(session, id);
        } else {
            logger.info("No logger related to such GameUser ID: {}", id);
        }
    }

    @Override
    public void notifyGameOver(Id <GameUser> id, int result) {
        GameWebSocket socket;
        if ((socket = userSockets.get(id)) != null) {
            socket.gameOver(result);
        } else {
            logger.info("No logger related to such GameUser ID: {}", id);
        }
    }
}