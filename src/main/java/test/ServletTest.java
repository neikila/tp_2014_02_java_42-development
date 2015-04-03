package test;

import Interface.AccountService;
import main.Context;
import resource.TestHelper;
import resource.ResourceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;

/**
 * Created by neikila on 30.03.15.
 */
public class ServletTest {
    final protected Context context = new Context();
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;
    final protected TestHelper testHelper = (TestHelper)(ResourceFactory.instance().getResource("helper"));
}
