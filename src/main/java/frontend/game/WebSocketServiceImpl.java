package frontend.game;

import Interface.WebSocketService;
import mechanics.GameUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    public void notifyMyNewScore(GameUser user) {
        userSockets.get(user.getMyName()).setMyScore(user);
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