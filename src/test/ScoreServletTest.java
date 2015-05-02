package test;

import static org.junit.Assert.*;
import main.accountService.AccountService;
import frontend.ScoreServlet;
import main.Context;
import main.user.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.mockito.Mockito.*;

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
        accountService = getAccountService();
        Context context = new Context();
        context.add(AccountService.class, accountService);
        servlet = new ScoreServlet(context);

        stringWriter = new StringWriter();
        response = getResponse(stringWriter);
    }


    //TODO вынести в исходны файл
    @Test
    public void testDoGet() throws Exception {
        createUsers();
        request = getRequest(null);
        when(request.getParameter("limit")).thenReturn(String.valueOf(4));
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

    @Test
    public void testDoGetWithWrongLimit() throws Exception {
        createUsers();
        request = getRequest(null);
        when(request.getParameter("limit")).thenReturn("asd");
        String correctAnswer = "{\"data\":{\"message\":\"WrongLimit\"},\"status\":400}";

        servlet.doGet(request, response);

        assertEquals("GetScore", correctAnswer, stringWriter.toString());
    }

    @Test
    public void testDoGetWithNoLimit() throws Exception {
        createUsers();
        request = getRequest(null);
        when(request.getParameter("limit")).thenReturn(null);
        String correctAnswer = "{\"data\":{\"message\":\"WrongLimit\"},\"status\":400}";

        servlet.doGet(request, response);

        assertEquals("GetScore", correctAnswer, stringWriter.toString());
    }
}