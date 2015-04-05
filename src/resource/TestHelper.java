package resource;

import Interface.AccountService;
import Interface.Resource;
import main.AccountServiceImpl;
import main.user.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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