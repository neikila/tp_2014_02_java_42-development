package test;

import frontend.SignInServlet;
import main.Context;
import main.accountService.AccountService;
import org.junit.Before;
import org.junit.Test;
import utils.Messages;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SignInServletTest extends ServletTest {
    private SignInServlet servlet;
    private Context context;

    private void setRequestReader(String login, String password) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(
                new StringReader(
                        "{\"login\":\"" + login + "\"," +
                        "\"password\":\"" + password + "\"}")));
    }

    @Before
    public void setUp() throws Exception {
        accountService = getAccountService(getUser());
        context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new SignInServlet(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }

    @Test
    public void testDoPost() throws Exception {
        String login = getUser().getLogin();
        request = getRequest(null);
        String CorrectResponse = "{\"data\":{\"login\":\""+ login +"\"},\"status\":200}";
        setRequestReader(login, getUser().getPassword());

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoLogin() throws Exception {
        String login = "";
        request = getRequest(null);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.wrongPasOrLogin() + "\"},\"status\":400}";
        setRequestReader(login, getUser().getPassword());

        servlet.doPost(request, response);

        assertEquals("SignInNoLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoPassword() throws Exception {
        String password = "";
        request = getRequest(null);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.wrongPasOrLogin() + "\"},\"status\":400}";
        setRequestReader(getUser().getLogin(), password);

        servlet.doPost(request, response);

        assertEquals("SignInNoPassword", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNotExist() throws Exception {
        request = getRequest(null);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.wrongPasOrLogin() + "\"},\"status\":400}";
        setRequestReader(getUser().getLogin() + "0", getUser().getLogin());

        servlet.doPost(request, response);

        assertEquals("SignInNoSuchUser", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfAlreadySignedIn() throws Exception {
        request = getRequest(null);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.alreadyLoggedIn() + "\"},\"status\":400}";
        setRequestReader(getUser().getLogin(), getUser().getPassword());
        accountService.addSessions(null, getUser());

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfBlocked() throws Exception {
        request = getRequest(null);
        context.setBlock();
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.block() + "\"},\"status\":400}";
        setRequestReader(getUser().getLogin(), getUser().getPassword());

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }
}