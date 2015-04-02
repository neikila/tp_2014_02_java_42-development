package resource;

import Interface.AccountService;
import Interface.Resource;
import main.AccountServiceImpl;
import main.UserProfile;

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
public class Helper implements Serializable, Resource{
//    private static Helper helper;
    private int limit = 4;
    private String sessionId = "1111";
    private UserProfile user = new UserProfile("test_test", "test_test", "test@test.test");
    private UserProfile admin = new UserProfile("admin", "admin", "admin@gmail.com");

    public HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    public HttpServletRequest getMockedRequest(String sessionId) {
        HttpSession httpSession = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(httpSession.getId()).thenReturn(sessionId);
        when(request.getSession()).thenReturn(httpSession);
        return request;
    }

    public AccountService setUpAccountServices(boolean havingUserIn) throws Exception {
        AccountService accountService = new AccountServiceImpl();
        if (havingUserIn) {
            accountService.addUser(user.getLogin(), user);
            accountService.addSessions(sessionId, user);
        }
        return accountService;
    }

    public String getSessionId() { return sessionId; }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public UserProfile getUser() { return user; }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public UserProfile getAdmin() {
        //admin.setAdmin(true);
        return admin;
    }

    public void setAdmin(UserProfile admin) {
        this.admin = admin;
        this.admin.setAdmin(true);
    }

    public int getLimit() { return limit; }

    public void setLimit(int limit) { this.limit = limit; }

    public Helper(int limit, UserProfile user, UserProfile admin, String sessionId) {
        this.admin = admin;
        this.user = user;
        this.limit = limit;
        this.sessionId = sessionId;
    }

    public Helper() {
        this.user = new UserProfile("test_test", "test_test", "test@test.test");;
        this.admin = new UserProfile("admin", "admin", "admin@gmail.com");
        this.limit = 0;
        this.sessionId = "";
    }

    public String toString() {
        return "Limit: " + limit + " SessionId: " + sessionId;
    }

    /*private Helper () {};

    public static Helper instance() {
        if (helper == null) {
            helper = new Helper();
        }
        return helper;
    }*/
}