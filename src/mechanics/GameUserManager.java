package mechanics;

import utils.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neikila on 10.05.15.
 */
public class GameUserManager {
    private Map<Id <GameUser> , GameUser> users = new HashMap<>();

    public GameUser getSelf(Id <GameUser> id) {
        return users.get(id);
    }

    public boolean addUser(GameUser user) {
        return users.put(user.getId(), user) != null;
    }

    public void removeUser(GameUser user) {
        users.remove(user.getId());
    }
}
