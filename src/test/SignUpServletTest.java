package test;

import frontend.SignUpServlet;
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

public class SignUpServletTest extends ServletTest {
    private SignUpServlet servlet;
    Context context;

    private void setRequestReader(String login, String password, String email) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(
                new StringReader(
                        "{\"login\":\"" + login + "\"," +
                        "\"password\":\"" + password + "\"," +
                        "\"email\":\"" + email+  "\"}")));
    }


    @Before
    public void setUp() throws Exception {
        accountService = getAccountService();
        context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new SignUpServlet(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }


    @Test
    public void testDoPost() throws Exception {
        String login = getUser().getLogin();
        String pass = getUser().getPassword();
        String email = getUser().getEmail();
        request = getRequest(null);
        setRequestReader(login, pass, email);
        String password = pass.substring(0, (pass.length() - 3)).replaceAll(".", "*")
                + pass.substring((pass.length() - 3), pass.length());
        String CorrectResponse = "{\"data\":" +
                "{\"password\":\"" + password + "\"," +
                "\"login\":\"" + login + "\"," +
                "\"email\":\"" + email + "\"}," +
                "\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignUp", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostWrongLogin() throws Exception {
        String login = "";
        String pass = getUser().getPassword();
        String email = getUser().getEmail();
        request = getRequest(null);
        setRequestReader(login, pass, email);

        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.wrongSignUpData() + "\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostWrongPassword() throws Exception {
        String login = getUser().getLogin();
        String pass = "";
        String email = getUser().getEmail();
        request = getRequest(null);
        setRequestReader(login, pass, email);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.wrongSignUpData() + "\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongPassword", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostWrongEmail() throws Exception {
        String login = getUser().getLogin();
        String pass = getUser().getPassword();
        String email = "";

        request = getRequest(null);
        setRequestReader(login, pass, email);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.wrongSignUpData() + "\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongEmail", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostAlreadyExist() throws Exception {
        String login = getUser().getLogin();
        String pass = getUser().getPassword();
        String email = getUser().getEmail();
        accountService.addUser(login, getUser());
        request = getRequest(null);
        setRequestReader(login, pass, email);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.exist() + "\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpAlreadyExist", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostAlreadySignedIn() throws Exception {
        String login = getUser().getLogin();
        String pass = getUser().getPassword();
        String email = getUser().getEmail();
        accountService.addUser(login, getUser());
        accountService.addSessions(null, getUser());
        request = getRequest(null);
        setRequestReader(login, pass, email);
        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.alreadyLoggedIn() + "\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpAlreadySignedIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfBlocked() throws Exception {
        String login = getUser().getLogin();
        String pass = getUser().getPassword();
        String email = getUser().getEmail();
        request = getRequest(null);
        setRequestReader(login, pass, email);
        context.setBlock();

        String CorrectResponse = "{\"data\":{\"message\":\"" + Messages.block() + "\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongEmail", CorrectResponse, stringWriter.toString());
    }
}