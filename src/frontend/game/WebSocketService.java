package frontend.game;

import mechanics.GameUser;
import org.json.simple.JSONObject;

/**
 * @author v.chibrikov
 */
public interface WebSocketService {

    void addUser(GameWebSocket user);

    void notifyMyNewScore(GameUser user);

    void notifyEnemyNewScore(GameUser user);

    void notifyStartGame(GameUser user, String sequence);

    void notifyGameOver(GameUser user, int result);

    void notifyAction(GameUser user, JSONObject action);

    void notifyResult(GameUser user, String result);
}
