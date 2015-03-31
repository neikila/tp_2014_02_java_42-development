package Test;

import Interface.AccountService;
import main.AccountServiceImpl;
import main.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by neikila on 30.03.15.
 */
public class Helper {
    private final static int limit = 4;
    private final static String sessionId = "1111";
    private final static UserProfile user = new UserProfile("test_test", "test_test", "test@test.test");
    private final static UserProfile admin = new UserProfile("admin", "admin", "admin@gmail.com");

    public static HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    public static HttpServletRequest getMockedRequest(String sessionId) {
        HttpSession httpSession = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(httpSession.getId()).thenReturn(sessionId);
        when(request.getSession()).thenReturn(httpSession);
        return request;
    }

    public static AccountService setUpAccountServices(boolean havingUserIn) throws Exception {
        AccountService accountService = new AccountServiceImpl();
        if (havingUserIn) {
            accountService.addUser(user.getLogin(), user);
            accountService.addSessions(sessionId, user);
        }
        return accountService;
    }

    public static String getSessionId() { return sessionId; }
    public static UserProfile getUser() { return user; }

    public static UserProfile getAdmin() {
        admin.setAdmin(true);
        return admin;
    }

    public static int getLimit() { return limit; }
}