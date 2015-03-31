package Test;

import Interface.AccountService;
import frontend.AdminServlet;
import frontend.ProfileServlet;
import main.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AdminServletTest extends ServletTest {
    private AdminServletTestExtension servlet;

    class AdminServletTestExtension extends AdminServlet {

        private boolean wasExecuted;

        AdminServletTestExtension(AccountService accountService1) {
            super(accountService1);
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
        accountService = Helper.setUpAccountServices(false);
        servlet = new AdminServletTestExtension(accountService);
        stringWriter = new StringWriter();
        response = Helper.getMockedResponse(stringWriter);
    }

    @Test
    public void testDoPost() throws Exception {
        UserProfile admin = Helper.getAdmin();
        accountService.addUser(admin.getLogin(), admin);
        accountService.addSessions(Helper.getSessionId(), admin);
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("action")).thenReturn("Stop server");

        servlet.doPost(request, response);

        assertEquals("StopServer", true, servlet.wasExecute());
    }
}