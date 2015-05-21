package mechanics;

import org.json.simple.JSONObject;

public interface GameMechanics {

    public void addUser(String user);

    public void incrementScore(GameUser user);

    public void analyzeMessage(String userName, JSONObject message);

    public void run();
}
