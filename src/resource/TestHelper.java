package resource;

import main.user.UserProfile;
import java.io.Serializable;

/**
 * Created by neikila on 30.03.15.
 */
public class TestHelper implements Serializable, Resource{
    private UserProfile user;
    private UserProfile admin;

    public UserProfile getUser() { return user; }

    public UserProfile getAdmin() {
        return admin;
    }

    @Override
    public void checkState() {
        if (!admin.isAdmin())
            admin.setAdmin(true);
    }
}