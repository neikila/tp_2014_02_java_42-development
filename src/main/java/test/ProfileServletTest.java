package test;

import Interface.AccountService;
import frontend.ProfileServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;

public class ProfileServletTest extends ServletTest {
    private ProfileServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = testHelper.setUpAccountServices(true);
        context.add(AccountService.class, accountService);
        servlet = new ProfileServlet(context);
        stringWriter = new StringWriter();
        response = testHelper.getMockedResponse(stringWriter);
    }

    @After
    public void tearDown() throws Exception {
        context.remove(AccountService.class);
    }

    @Test
    public void testDoGet() throws Exception {
        String login = testHelper.getUser().getLogin();
        String pass = testHelper.getUser().getPassword();
        String email = testHelper.getUser().getEmail();
        int score = testHelper.getUser().getScore();
        String role = testHelper.getUser().getRole();

        request = testHelper.getMockedRequest(testHelper.getSessionId());

        String password = pass.substring(0, (pass.length() - 3)).replaceAll(".", "*")
                + pass.substring((pass.length() - 3), pass.length());
        String CorrectResponse = "{\"data\":" +
                "{\"score\":" + score + "," +
                "\"password\":\"" + password + "\"," +
                "\"role\":\"" + role + "\"," +
                "\"login\":\"" + login + "\"," +
                "\"email\":\"" + email + "\"}," +
                "\"status\":200}";

        servlet.doGet(request, response);

        assertEquals("Profile", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoGetIfNotSignedIn() throws Exception {
        String WrongSessionId = testHelper.getSessionId() + "1";
        request = testHelper.getMockedRequest(WrongSessionId);
        String CorrectResponse = "{\"data\":{\"message\":\"Unauthorized\"},\"status\":401}";

        servlet.doGet(request, response);

        assertEquals("ProfileNotSignedIn", CorrectResponse, stringWriter.toString());
    }
}