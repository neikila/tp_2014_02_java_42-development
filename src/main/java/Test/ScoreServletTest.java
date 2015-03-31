package Test;

import frontend.ScoreServlet;
import main.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.when;

public class ScoreServletTest extends ServletTest {
    private ScoreServlet servlet;

    private void createUsers() {
        UserProfile Vas = new UserProfile("Vasya", "Vasya", "Vasya@gmail.com");
        Vas.setScore(14);
        accountService.addUser(Vas.getLogin(), Vas);
        UserProfile Van = new UserProfile("Vanya", "Vanya", "Vanya@gmail.com");
        Van.setScore(12);
        accountService.addUser(Van.getLogin(), Van);
        UserProfile Pet = new UserProfile("Petya", "Petya", "Petya@gmail.com");
        Pet.setScore(10);
        accountService.addUser(Pet.getLogin(), Pet);
        UserProfile Dan = new UserProfile("Danya", "Danya", "Danya@gmail.com");
        Dan.setScore(2);
        accountService.addUser(Dan.getLogin(), Dan);
    }


    @Before
    public void setUp() throws Exception {
        accountService = Helper.setUpAccountServices(false);
        servlet = new ScoreServlet(accountService);
        stringWriter = new StringWriter();
        response = Helper.getMockedResponse(stringWriter);
    }

    @Test
    public void testDoGet() throws Exception {
        createUsers();
        request = Helper.getMockedRequest(Helper.getSessionId());
        when(request.getParameter("limit")).thenReturn("4");
        String correctAnswer = "{\"data\":" +
                "{\"scoreList\":[" +
                                    "{\"score\":14,\"login\":\"Vasya\"}," +
                                    "{\"score\":12,\"login\":\"Vanya\"}," +
                                    "{\"score\":10,\"login\":\"Petya\"}," +
                                    "{\"score\":2,\"login\":\"Danya\"}" +
                "]}," +
                "\"status\":200}";

        servlet.doGet(request, response);

        assertEquals("GetScore", correctAnswer, stringWriter.toString());
    }
}