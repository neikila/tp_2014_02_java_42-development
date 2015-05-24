package test;

import main.Context;
import main.accountService.AccountService;
import main.accountService.AccountServiceImpl;
import main.user.UserProfile;

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

public class ServletTest {
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;

    public ServletTest() {
    }

    public AccountService getAccountService() {
        return new AccountServiceImpl(new Context());
    }

    public AccountService getAccountService(UserProfile user) {
        AccountService accountService = getAccountService();
        accountService.addUser(user.getLogin(), user);
        return accountService;
    }

    public AccountService getAccountServiceWithSession(UserProfile user) {
        AccountService accountService = getAccountService();
        accountService.addUser(user.getLogin(), user);
        accountService.addSessions(null, user);
        return accountService;
    }

    public HttpServletRequest getRequest(String sessionId) {
        HttpSession httpSession = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(httpSession.getId()).thenReturn(sessionId);
        when(request.getSession()).thenReturn(httpSession);
        return request;
    }

    public HttpServletResponse getResponse(StringWriter stringWriter) throws IOException{
        HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    public UserProfile getUser() {
        return new UserProfile("test_test", "test_test", "test@test.test");
    }

    public UserProfile getAdmin() {
        UserProfile admin = new UserProfile("admin", "admin", "admin@gmail.com");
        admin.setAdmin(true);
        return admin;
    }
}