package test;

import Interface.AccountService;
import main.AccountServiceImpl;
import main.user.UserProfile;
import resource.TestHelper;
import resource.ResourceFactory;

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
 *
 */

public class ServletTest {
    final private TestHelper testHelper;
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;

    public ServletTest() {
        testHelper = (TestHelper)(ResourceFactory.instance().getResource("helper"));
    }

    public AccountService getAccountService() {
        AccountService accountService = new AccountServiceImpl();
        return accountService;
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
        return testHelper.getUser();
    }

    public UserProfile getAdmin() {
        return testHelper.getAdmin();
    }
}