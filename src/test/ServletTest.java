package test;

import main.accountService.AccountService;
import main.accountService.AccountServiceImpl;
import main.user.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by neikila on 30.03.15.
 */

public class ServletTest {
//    final protected Messages messages;
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;

    public ServletTest() {

//        messages = mock(Messages.class); //(Messages) ResourceFactory.instance().getResource("messages")
//
//        when(messages.adminPage()).thenReturn("Admin page");
//        when(messages.notFound()).thenReturn("404: Not Found.");
//        when(messages.wrongParamAction()).thenReturn("Wrong 'action'");
//        when(messages.notAuthorised()).thenReturn("Not Authorised");
//        when(messages.logInStatus()).thenReturn("Log In:");
//        when(messages.alreadyLoggedIn()).thenReturn("You has already Logged In");
//        when(messages.block()).thenReturn("Authorization and signing up is blocked");
//        when(messages.wrongPasOrLogin()).thenReturn("Wrong password or login");
//        when(messages.fillAllTheGaps()).thenReturn("Fill all the gaps, please:");
//        when(messages.logOutFirst()).thenReturn("You have to logout before signing up.");
//        when(messages.exist()).thenReturn("Such user already exists");
//        when(messages.wrongSignUpData()).thenReturn("Wrong sign Up data");
//        when(messages.wrongLimit()).thenReturn("WrongLimit");
//        when(messages.askToSignIn()).thenReturn("You haven't signed up yet. Please, do it.");
//        when(messages.JSONStatusIncrement()).thenReturn("increment");
//        when(messages.JSONStatusFinish()).thenReturn("finish");
//        when(messages.JSONStatusResult()).thenReturn("result");
//        when(messages.JSONStatusStart()).thenReturn("start");
//        when(messages.JSONStatusSettings()).thenReturn("settings");

    }

    public AccountService getAccountService() {
        AccountService accountService = new AccountServiceImpl();
        return accountService;
    }

    public AccountService getAccountService(UserProfile user) {
        AccountService accountService = getAccountService();
        accountService.addUser(user.getLogin(), user);
        return accountService;
    }

    public AccountService getAccountServiceWithSession(UserProfile user) {
        AccountService accountService = getAccountService();
        accountService.addUser(user.getLogin(), user);
        accountService.addSessions(null, user);
        return accountService;
    }

    public HttpServletRequest getRequest(String sessionId) {
        HttpSession httpSession = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(httpSession.getId()).thenReturn(sessionId);
        when(request.getSession()).thenReturn(httpSession);
        return request;
    }

    public HttpServletResponse getResponse(StringWriter stringWriter) throws IOException{
        HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    public UserProfile getUser() {
        return new UserProfile("test_test", "test_test", "test@test.test");
    }

    public UserProfile getAdmin() {
        UserProfile admin = new UserProfile("admin", "admin", "admin@gmail.com");
        admin.setAdmin(true);
        return admin;
    }
}