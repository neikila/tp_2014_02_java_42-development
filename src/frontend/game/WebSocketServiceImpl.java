package frontend.game;

import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import org.json.simple.JSONObject;
import utils.Id;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private Map<Id <GameUser>, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getId(), user);
    }

    public void sendSettings(GameUser user, GameMap map) {
        userSockets.get(user.getId()).settings(map);
    }

    public void notifyAction(GameUser user, JSONObject action) {
        GameWebSocket gameWebSocket = userSockets.get(user.getId());
        gameWebSocket.sendAction(action);
    }

    public void notifyMyNewScore(GameUser user) {
        userSockets.get(user.getId()).setMyScore(user);
    }

    public void notifyEnemyNewScore(GameSession session, int position) {
        userSockets.get(session.getSelf(position).getId()).setMyScore(session.getEnemy(position));
    }

    public void notifyStartGame(GameSession session, int position) {
        GameWebSocket gameWebSocket = userSockets.get(session.getSelf(position).getId());
        gameWebSocket.startGame(session, position);
    }

    @Override
    public void notifyGameOver(GameUser user, int result) {
        userSockets.get(user.getId()).gameOver(user, result);
    }

    @Override
    public void notifyResult(GameUser user, String result) {
        userSockets.get(user.getId()).setMyResult(result);
    }
}