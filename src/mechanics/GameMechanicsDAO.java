package mechanics;

import main.user.UserProfile;
import org.json.simple.JSONObject;
import utils.Id;

public interface GameMechanicsDAO {

    public void addUser(Id<GameUser> id, UserProfile user);

    public void incrementScore(GameUser user);

    public void analyzeMessage(Id<GameUser> id, JSONObject message);

    public void gmStep();

    public void setShellAbove(GameMechanics shell);
}
