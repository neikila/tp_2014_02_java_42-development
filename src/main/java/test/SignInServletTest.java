package test;

import Interface.AccountService;
import frontend.ProfileServlet;
import frontend.SignInServlet;
import main.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SignInServletTest extends ServletTest {
    private SignInServlet servlet;
    private Context context;

    @Before
    public void setUp() throws Exception {
        accountService = getAccountService(true);
        context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new SignInServlet(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }


    @Test
    public void testDoPost() throws Exception {
        accountService.removeSession(null);
        String login = getUser().getLogin();
        String password = getUser().getPassword();
        request = getRequest(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"login\":\""+ login +"\"},\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoLogin() throws Exception {
        accountService.removeSession(null);
        String login = "";
        String password = getUser().getPassword();
        request = getRequest(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoPassword() throws Exception {
        accountService.removeSession(null);
        String login = getUser().getLogin();
        String password = "";
        request = getRequest(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoPassword", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNotExist() throws Exception {
        accountService.removeSession(null);
        String login = "testWrong";
        String password = "testWrong";
        request = getRequest(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoSuchUser", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfAlreadySignedIn() throws Exception {
        String login = getUser().getLogin();
        String password = getUser().getPassword();
        request = getRequest(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Already\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfBlocked() throws Exception {
        accountService.removeSession(null);
        String login = getUser().getLogin();
        String password = getUser().getPassword();
        request = getRequest(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        context.setBlock();
        String CorrectResponse = "{\"data\":{\"message\":\"Blocked\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }
}