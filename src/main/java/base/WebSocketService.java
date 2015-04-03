package base;

import frontend.game.GameWebSocket;

/**
 * @author v.chibrikov
 */
public interface WebSocketService {

    void addUser(GameWebSocket user);

    void notifyMyNewScore(GameUser user);

    void notifyEnemyNewScore(GameUser user);

    void notifyStartGame(GameUser user, String sequence);

    void notifyGameOver(GameUser user, int result);

    void notifyResult(GameUser user, String result);
}
