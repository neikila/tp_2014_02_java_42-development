package frontend.game;

import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class WebSocketServiceImpl implements base.WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    public void notifyMyNewScore(base.GameUser user) {
        userSockets.get(user.getMyName()).setMyScore(user);
    }

    public void notifyEnemyNewScore(base.GameUser user) {
        userSockets.get(user.getMyName()).setEnemyScore(user);
    }

    public void notifyStartGame(base.GameUser user, String sequence) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.startGame(user, sequence);
    }

    @Override
    public void notifyGameOver(base.GameUser user, int result) {
        userSockets.get(user.getMyName()).gameOver(user, result);
    }

    @Override
    public void notifyResult(base.GameUser user, String result) {
        userSockets.get(user.getMyName()).setMyResult(result);
    }
}