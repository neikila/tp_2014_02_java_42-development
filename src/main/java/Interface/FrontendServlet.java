package Interface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by neikila on 29.03.15.
 */
public interface FrontendServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException;

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException;
}