package test;

import frontend.AdminServlet;
import main.Context;
import main.accountService.AccountService;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AdminServletTest extends ServletTest {
    private AdminServletTestExtension servlet;

    class AdminServletTestExtension extends AdminServlet {

        private boolean wasExecuted;

        AdminServletTestExtension(Context context) {
            super(context);
            wasExecuted = false;
        }

        @Override
        public void StopServers() {
            wasExecuted = true;
        }

        public boolean wasExecute() {
            return wasExecuted;
        }
    }

    @Before
    public void setUp() throws Exception {
        accountService = getAccountServiceWithSession(getAdmin());
        Context context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new AdminServletTestExtension(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);

    }

    @Test
    public void testDoPost() throws Exception {
        request = getRequest(null);
        when(request.getParameter("action")).thenReturn("stop");

        servlet.doPost(request, response);

        assertEquals("StopServer", true, servlet.wasExecute());
    }

    @Test
    public void testDoPostIfNotAdmin() throws Exception {
        String sessionId = "";
        request = getRequest(sessionId);
        when(request.getParameter("action")).thenReturn("stop");
        accountService.addUser(getUser().getLogin(), getUser());
        accountService.addSessions(sessionId, getUser());

        servlet.doPost(request, response);

        assertEquals("DoPostIfNotAdmin", false, servlet.wasExecute());
    }

    @Test
    public void testDoPostIfNotAuthorized() throws Exception {
        request = getRequest(null);
        when(request.getParameter("action")).thenReturn("stop");
        accountService.removeSession(null);

        servlet.doPost(request, response);

        assertEquals("DoPostIfNotAuthorized", false, servlet.wasExecute());
    }

}