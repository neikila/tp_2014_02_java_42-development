package frontend.game;

import mechanics.GameError;
import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import messageSystem.Abonent;
import org.json.simple.JSONObject;
import utils.Id;

public interface WebSocketService extends Abonent, Runnable{

    void addUser(GameWebSocket user);

    GameWebSocket getSocket(Id<GameUser> id);

    void sendSettings(Id<GameUser> id, GameMap map);

    void notifyStartGame(GameSession session, Id <GameUser> id);

    void sync(GameSession session, Id <GameUser> id);

    void notifyGameOver(Id <GameUser> id, int result);

    void notifyAction(Id <GameUser> id, JSONObject action);

    void notifyOpponentLostConnection(Id <GameUser> id, GameError gameError);

    void removeUser(Id <GameUser> id);
}
