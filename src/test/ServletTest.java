package test;

import main.Context;
import main.accountService.AccountServiceDAO;
import main.accountService.AccountServiceDAOImpl;
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
    protected AccountServiceDAO accountServiceDAO;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;

    public ServletTest() {
    }

    public AccountServiceDAO getAccountServiceDAO() {
        return new AccountServiceDAOImpl(new Context());
    }

    public AccountServiceDAO getAccountService(UserProfile user) {
        AccountServiceDAO accountServiceDAO = getAccountServiceDAO();
        accountServiceDAO.addUser(user.getLogin(), user);
        return accountServiceDAO;
    }

    public AccountServiceDAO getAccountServiceWithSession(UserProfile user) {
        AccountServiceDAO accountServiceDAO = getAccountServiceDAO();
        accountServiceDAO.addUser(user.getLogin(), user);
        accountServiceDAO.addSessions(null, user);
        return accountServiceDAO;
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