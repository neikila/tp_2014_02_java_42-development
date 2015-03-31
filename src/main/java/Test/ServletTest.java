package Test;

import Interface.AccountService;
import frontend.SignInServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;

/**
 * Created by neikila on 30.03.15.
 */
public class ServletTest {
    protected AccountService accountService;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter stringWriter;
}
