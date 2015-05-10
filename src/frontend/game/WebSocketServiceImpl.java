package frontend.game;

import mechanics.GameMap;
import mechanics.GameUser;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    public void sendSettings(String name, GameMap map) {
        userSockets.get(name).settings(map);
    }

    public void notifyMyNewScore(GameUser user) {
        userSockets.get(user.getMyName()).setMyScore(user);
    }

    public void notifyAction(GameUser user, JSONObject action) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.sendAction(action);
    }

    public void notifyEnemyNewScore(GameUser user) {
        userSockets.get(user.getMyName()).setEnemyScore(user);
    }

    public void notifyStartGame(GameUser user, String sequence) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.startGame(user, sequence);
    }

    @Override
    public void notifyGameOver(GameUser user, int result) {
        userSockets.get(user.getMyName()).gameOver(user, result);
    }

    @Override
    public void notifyResult(GameUser user, String result) {
        userSockets.get(user.getMyName()).setMyResult(result);
    }
}