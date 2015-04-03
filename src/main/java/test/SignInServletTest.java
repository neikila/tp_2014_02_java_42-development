package test;

import Interface.AccountService;
import frontend.SignInServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SignInServletTest extends ServletTest {
    private SignInServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = testHelper.setUpAccountServices(true);
        context.add(AccountService.class, accountService);
        servlet = new SignInServlet(context);
        stringWriter = new StringWriter();
        response = testHelper.getMockedResponse(stringWriter);
    }

    @After
    public void tearDown() throws Exception {
        context.remove(AccountService.class);
    }

    @Test
    public void testDoPost() throws Exception {
        accountService.removeSession(testHelper.getSessionId());
        String login = testHelper.getUser().getLogin();
        String password = testHelper.getUser().getPassword();
        request = testHelper.getMockedRequest(testHelper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"login\":\""+ login +"\"},\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoLogin() throws Exception {
        accountService.removeSession(testHelper.getSessionId());
        String login = "";
        String password = testHelper.getUser().getPassword();
        request = testHelper.getMockedRequest(testHelper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoLogin", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNoPassword() throws Exception {
        accountService.removeSession(testHelper.getSessionId());
        String login = testHelper.getUser().getLogin();
        String password = "";
        request = testHelper.getMockedRequest(testHelper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoPassword", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfNotExist() throws Exception {
        accountService.removeSession(testHelper.getSessionId());
        String login = "testWrong";
        String password = "testWrong";
        request = testHelper.getMockedRequest(testHelper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Wrong\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignInNoSuchUser", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfAlreadySignedIn() throws Exception {
        String login = testHelper.getUser().getLogin();
        String password = testHelper.getUser().getPassword();
        request = testHelper.getMockedRequest(testHelper.getSessionId());
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("login")).thenReturn(login);
        String CorrectResponse = "{\"data\":{\"message\":\"Already\"},\"status\":400}";

        servlet.doPost(request, response);

        assertEquals("SignIn", CorrectResponse, stringWriter.toString());
    }
}