package Test;

import Interface.AccountService;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SignUpServletTest extends ServletTest {
    private SignUpServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = Helper.setUpAccountServices(false);
        context.add(AccountService.class, accountService);
        servlet = new SignUpServlet(context);
        stringWriter = new StringWriter();
        response = Helper.getMockedResponse(stringWriter);
    }

    @After
    public void tearDown() throws Exception {
        context.remove(AccountService.class);
    }

    @Test
    public void testDoPost() throws Exception {
        String login = Helper.getUser().getLogin();
        String pass = Helper.getUser().getPassword();
        String email = Helper.getUser().getEmail();

        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(pass);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(email);
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
        String login = "test";
        String pass = Helper.getUser().getPassword();
        String email = Helper.getUser().getEmail();

        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(pass);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(email);

        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostWrongPassword() throws Exception {
        String login = Helper.getUser().getLogin();
        String pass = "test";
        String email = Helper.getUser().getEmail();

        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(pass);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(email);

        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostWrongEmail() throws Exception {
        String login = Helper.getUser().getLogin();
        String pass = Helper.getUser().getPassword();
        String email = "test";

        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(pass);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(email);

        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostAlreadyExist() throws Exception {
        String login = Helper.getUser().getLogin();
        String pass = Helper.getUser().getPassword();
        String email = Helper.getUser().getEmail();

        accountService.addUser(login, Helper.getUser());

        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(pass);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(email);

        String CorrectResponse = "{\"data\":{\"message\":\"Exist\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostAlreadySignedIn() throws Exception {
        String login = Helper.getUser().getLogin();
        String pass = Helper.getUser().getPassword();
        String email = Helper.getUser().getEmail();

        accountService.addUser(login, Helper.getUser());
        accountService.addSessions(Helper.getSessionId(), Helper.getUser());

        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("password")).thenReturn(pass);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(email);

        String CorrectResponse = "{\"data\":{\"message\":\"Already\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignUpWrongLogin", CorrectResponse, stringWriter.toString());
    }
}