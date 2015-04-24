package test;

import Interface.AccountService;
import frontend.SignOutServlet;
import main.Context;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class SignOutServletTest extends ServletTest{
    private SignOutServlet servlet;

    @Before
    public void setUp() throws Exception {
        accountService = getAccountServiceWithSession(getUser());
        Context context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new SignOutServlet(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }


    @Test
    public void testDoPost() throws Exception {
        request = getRequest(null);
        String CorrectResponse = "{\"data\":{},\"status\":200}";

        servlet.doPost(request, response);

        assertEquals("SignOut", CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testDoPostIfItIsNotExist() throws Exception {
        String WrongSessionId = "";
        request = getRequest(WrongSessionId);
        String CorrectResponse = "{\"data\":{\"message\":\"" + messages.notAuthorised() + "\"},\"status\":401}";

        servlet.doPost(request, response);

        assertEquals("SignOutIfNotExist", CorrectResponse, stringWriter.toString());
    }
}