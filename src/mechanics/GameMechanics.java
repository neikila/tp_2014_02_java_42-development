package mechanics;

import main.user.UserProfile;
import messageSystem.Abonent;
import org.json.simple.JSONObject;
import utils.Id;

public interface GameMechanics extends Abonent, Runnable{

    public void addUser(Id<GameUser> id, UserProfile user);

    public void incrementScore(GameUser user);

    public void analyzeMessage(Id<GameUser> id, JSONObject message);

    public void removeSession(GameSession session);
}
