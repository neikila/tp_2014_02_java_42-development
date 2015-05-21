package frontend.game;

import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import org.json.simple.JSONObject;

/**
 * @author v.chibrikov
 */
public interface WebSocketService {

    void addUser(GameWebSocket user);

    void sendSettings(GameUser user, GameMap map);

    void notifyMyNewScore(GameUser user);

    void notifyEnemyNewScore(GameSession session, int gameUserPosition);

    void notifyStartGame(GameSession session, int gameUserPosition);

    void notifyGameOver(GameUser user, int result);

    void notifyAction(GameUser user, JSONObject action);

    void notifyResult(GameUser user, String result);
}
