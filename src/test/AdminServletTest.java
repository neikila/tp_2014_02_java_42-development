package test;

import Interface.AccountService;
import frontend.AdminServlet;
import frontend.ProfileServlet;
import main.Context;
import main.user.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
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
        accountService = getAccountService(false);
        Context context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new AdminServletTestExtension(context);
        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }

    @Test
    public void testDoPost() throws Exception {
        UserProfile admin = getAdmin();
        accountService.addUser(admin.getLogin(), admin);
        accountService.addSessions(null, admin);
        request = getRequest(null);
        when(request.getParameter("action")).thenReturn("stop");

        servlet.doPost(request, response);

        assertEquals("StopServer", true, servlet.wasExecute());
    }
}