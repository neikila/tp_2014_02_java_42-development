package Test;

import frontend.ProfileServlet;
import frontend.SignUpServlet;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ProfileServletTest extends ServletTest {
    private ProfileServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = Helper.setUpAccountServices(true);
        servlet = new ProfileServlet(accountService);
        stringWriter = new StringWriter();
        response = Helper.getMockedResponse(stringWriter);
    }

    @Test
    public void testDoGet() throws Exception {
        String login = Helper.getUser().getLogin();
        String pass = Helper.getUser().getPassword();
        String email = Helper.getUser().getEmail();
        int score = Helper.getUser().getScore();
        String role = Helper.getUser().getRole();

        request = Helper.getMockedRequest(Helper.getSessionId());

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
        String WrongSessionId = Helper.getSessionId() + "1";
        request = Helper.getMockedRequest(WrongSessionId);
        String CorrectResponse = "{\"data\":{\"message\":\"Unauthorized\"},\"status\":401}";

        servlet.doGet(request, response);

        assertEquals("ProfileNotSignedIn", CorrectResponse, stringWriter.toString());
    }
}