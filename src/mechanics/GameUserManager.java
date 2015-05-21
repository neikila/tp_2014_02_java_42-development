package mechanics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neikila on 10.05.15.
 */
public class GameUserManager {
    private Map<String, GameUser> users = new HashMap<>();

    public GameUser getSelf(String user) {
        return users.get(user);
    }

    public void addUser(GameUser user) {
        users.put(user.getMyName(), user);
    }

    public void removeUser(GameUser user) {
        users.remove(user.getMyName());
    }
}
