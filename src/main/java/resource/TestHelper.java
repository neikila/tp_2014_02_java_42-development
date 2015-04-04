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
    private int limit;
    private String sessionId;
    private UserProfile user;
    private UserProfile admin;

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

    public UserProfile getUser() { return user; }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public UserProfile getAdmin() {
        return admin;
    }


    public int getLimit() { return limit; }

    public TestHelper(int limit, UserProfile user, UserProfile admin, String sessionId) {
        this.admin = admin;
        this.user = user;
        this.limit = limit;
        this.sessionId = sessionId;
    }

    public TestHelper() {
        this.user = new UserProfile("test_test", "test_test", "test@test.test");;
        this.admin = new UserProfile("admin", "admin", "admin@gmail.com");
        admin.setAdmin(true);
        this.limit = 4;
        this.sessionId = "";
    }

    public String toString() {
        return "Limit: " + limit + " SessionId: " + sessionId;
    }

    @Override
    public void checkState() {
        if (limit < 1)
            limit = 1;
        if (!admin.isAdmin())
            admin.setAdmin(true);
    }
}