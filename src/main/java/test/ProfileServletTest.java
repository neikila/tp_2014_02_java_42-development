package test;

import Interface.AccountService;
import frontend.ProfileServlet;
import main.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;

public class ProfileServletTest extends ServletTest {

    private ProfileServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = getAccountService(true);
        Context context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new ProfileServlet(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }

    @Test
    public void testDoGet() throws Exception {
        String login = getUser().getLogin();
        String pass = getUser().getPassword();
        String email = getUser().getEmail();
        int score = getUser().getScore();
        String role = getUser().getRole();

        request = getRequest(null);

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
        String WrongSessionId = "";
        request = getRequest(WrongSessionId);
        String CorrectResponse = "{\"data\":{\"message\":\"Unauthorized\"},\"status\":401}";

        servlet.doGet(request, response);

        assertEquals("ProfileNotSignedIn", CorrectResponse, stringWriter.toString());
    }
}