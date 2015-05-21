package frontend.game;

import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    public void sendSettings(GameUser user, GameMap map) {
        userSockets.get(user.getMyName()).settings(map);
    }

    public void notifyAction(GameUser user, JSONObject action) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.sendAction(action);
    }

    public void notifyMyNewScore(GameUser user) {
        userSockets.get(user.getMyName()).setMyScore(user);
    }

    public void notifyEnemyNewScore(GameSession session, int position) {
        userSockets.get(session.getSelf(position).getMyName()).setMyScore(session.getEnemy(position));
    }

    public void notifyStartGame(GameSession session, int position) {
        GameWebSocket gameWebSocket = userSockets.get(session.getSelf(position).getMyName());
        gameWebSocket.startGame(session, position);
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