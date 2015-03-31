package Test;

import frontend.SignInServlet;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SignInServletTest extends ServletTest {
    private SignInServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = Helper.setUpAccountServices(true);
        servlet = new SignInServlet(accountService);
        stringWriter = new StringWriter();
        response = Helper.getMockedResponse(stringWriter);
    }

    @Test
    public void testDoPost() throws Exception {
        accountService.removeSession(Helper.getSessionId());
        String login = Helper.getUser().getLogin();
        String password = Helper.getUser().getPassword();
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"login\":\""+ login +"\"},\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoLogin() throws Exception {
        accountService.removeSession(Helper.getSessionId());
        String login = "";
        String password = Helper.getUser().getPassword();
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoPassword() throws Exception {
        accountService.removeSession(Helper.getSessionId());
        String login = Helper.getUser().getLogin();
        String password = "";
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoPassword", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNotExist() throws Exception {
        accountService.removeSession(Helper.getSessionId());
        String login = "testWrong";
        String password = "testWrong";
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoSuchUser", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfAlreadySignedIn() throws Exception {
        String login = Helper.getUser().getLogin();
        String password = Helper.getUser().getPassword();
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Already\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }
}