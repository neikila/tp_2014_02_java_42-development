package test;

import Interface.AccountService;
import main.Context;
import main.user.UserProfile;
import resource.TestHelper;
import resource.ResourceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by neikila on 30.03.15.
 *
 */

public class ServletTest {
    final private TestHelper testHelper;
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;

    public ServletTest() {
        testHelper = (TestHelper)(ResourceFactory.instance().getResource("helper"));
    }

    public AccountService getAccountService(boolean havingUserIn) {
        return testHelper.setUpAccountServices(havingUserIn);
    }

    public HttpServletRequest getRequest(String sessionId) {
        return testHelper.getMockedRequest(sessionId);
    }

    public HttpServletResponse getResponse(StringWriter stringWriter) throws IOException{
        return testHelper.getMockedResponse(stringWriter);
    }

    public UserProfile getUser() {
        return testHelper.getUser();
    }

    public UserProfile getAdmin() {
        return testHelper.getAdmin();
    }
}