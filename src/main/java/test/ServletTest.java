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
//TODO TestHelper
public class ServletTest {
    final protected Context context;
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;
    final protected TestHelper testHelper;

    public ServletTest() {
        context = new Context();
        testHelper = (TestHelper)(ResourceFactory.instance().getResource("helper"));
    }

    public HttpServletRequest getRequest(String sessionId) {
        return testHelper.getMockedRequest(sessionId);
    }

    public HttpServletResponse getRequest(StringWriter stringWriter) throws IOException{
        return testHelper.getMockedResponse(stringWriter);
    }

    public UserProfile getUser() {
        return testHelper.getUser();
    }

    public UserProfile getAdmin() {
        return testHelper.getAdmin();
    }
}
