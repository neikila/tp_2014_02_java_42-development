package test;

import Interface.AccountService;
import frontend.SignOutServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class SignOutServletTest extends ServletTest{
    private SignOutServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = testHelper.setUpAccountServices(true);
        context.add(AccountService.class, accountService);
        servlet = new SignOutServlet(context);
        stringWriter = new StringWriter();
        response = testHelper.getMockedResponse(stringWriter);
    }

    @After
    public void tearDown() throws Exception {
        context.remove(AccountService.class);
    }

    @Test
    public void testDoPost() throws Exception {
        request = testHelper.getMockedRequest(testHelper.getSessionId());
        String CorrectResponse = "{\"data\":{},\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignOut", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfItIsNotExist() throws Exception {
        String WrongSessionId = testHelper.getSessionId() + "1";
        request = testHelper.getMockedRequest(WrongSessionId);
        String CorrectResponse = "{\"data\":{\"message\":\"Unauthorized\"},\"status\":401}";

        servlet.doPost(request, response);

        assertEquals("SignOutIfNotExist", CorrectResponse, stringWriter.toString());
    }
}