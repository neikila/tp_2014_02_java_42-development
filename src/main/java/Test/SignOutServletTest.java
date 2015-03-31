package Test;

import frontend.SignOutServlet;
import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class SignOutServletTest extends ServletTest{
    private SignOutServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = Helper.setUpAccountServices(true);
        servlet = new SignOutServlet(accountService);
        stringWriter = new StringWriter();
        response = Helper.getMockedResponse(stringWriter);
    }

    @Test
    public void testDoPost() throws Exception {
        request = Helper.getMockedRequest(Helper.getSessionId());
        String CorrectResponse = "{\"data\":{},\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignOut", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfItIsNotExist() throws Exception {
        String WrongSessionId = Helper.getSessionId() + "1";
        request = Helper.getMockedRequest(WrongSessionId);
        String CorrectResponse = "{\"data\":{\"message\":\"Unauthorized\"},\"status\":401}";

        servlet.doPost(request, response);

        assertEquals("SignOutIfNotExist", CorrectResponse, stringWriter.toString());
    }
}