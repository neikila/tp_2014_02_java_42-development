package test;

import main.AccountServiceImpl;
import Interface.AccountService;
import main.user.UserComparatorByScore;
import main.user.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resource.TestHelper;
import resource.ResourceFactory;

import java.util.TreeSet;

import static org.junit.Assert.*;

// TODO переделать взятие пользователей из класса Helper

public class AccountServiceTest {
    final private TestHelper testHelper = (TestHelper)(ResourceFactory.instance().getResource("helper"));
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddUser() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountService.addUser("test1234", user);

        assertEquals(1, accountService.getAmountOfUsers());
    }

    @Test
    public void testAddUserIfAlreadyExist() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountService.addUser("test1234", user);
        accountService.addUser("test1234", user);

        assertEquals(1, accountService.getAmountOfUsers());
    }


    @Test
    public void testAddSessions() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountService.addSessions("test1234", user);

        assertEquals(accountService.getAmountOfSessions(), 1);
        assertEquals(accountService.getAmountOfSessionsWitUserAsKey(), 1);
    }

    @Test
    public void testAddSessionsIfAlreadyExist() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountService.addSessions("test1234", user);
        accountService.addSessions("test1234", user);

        assertEquals(accountService.getAmountOfSessions(), 1);
        assertEquals(accountService.getAmountOfSessionsWitUserAsKey(), 1);
    }

    @Test
    public void testGetAmountOfSessions() throws Exception {
        //TODO Пока что это просто getter в дальнейшем обращение к базе
    }

    @Test
    public void testGetAmountOfUsers() throws Exception {
        //TODO Пока что это просто getter в дальнейшем обращение к базе
    }

    @Test
    public void testIsSessionWithSuchLoginExist() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountService.addSessions("test1234", user);

        boolean resultTrue = accountService.isSessionWithSuchLoginExist("test1234");
        boolean resultFalse = accountService.isSessionWithSuchLoginExist("test1235");

        assertEquals(resultTrue, true);
        assertEquals(resultFalse, false);
    }

    @Test
    public void testGetUser() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountService.addUser("test1234", user);

        UserProfile resultUserTrue = accountService.getUser("test1234");
        UserProfile resultUserFalse = accountService.getUser("test1235");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testGetSessions() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountService.addSessions("111111", user);

        UserProfile resultUserTrue = accountService.getSessions("111111");
        UserProfile resultUserFalse = accountService.getSessions("121212");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testGetSessionsByLogin() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountService.addSessions("111111", user);

        UserProfile resultUserTrue = accountService.getSessionsByLogin("test1234");
        UserProfile resultUserFalse = accountService.getSessionsByLogin("test1235");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testRemoveSession() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountService.addSessions("111111", user);

        accountService.removeSession("111111");

        assertEquals(accountService.getAmountOfSessions(), 0);
        assertEquals(accountService.getAmountOfSessionsWitUserAsKey(), 0);
    }

    @Test
    public void testCreateAdmin() throws Exception {
        String login = "admin";
        String password = "admin";
        String email = "admin@gmail.com";
        UserProfile profileAdmin = new UserProfile(login, password, email);
        profileAdmin.setAdmin(true);
        profileAdmin.setScore(1000);

        accountService.createAdmin();

        UserProfile resultUserAdmin = accountService.getUser("admin");

        assertEquals(resultUserAdmin, profileAdmin);
    }

    @Test
    public void testGetFirstByScore() throws Exception {
        UserComparatorByScore comp = new UserComparatorByScore();
        TreeSet<UserProfile> FirstFour = new TreeSet<>(comp);

        UserProfile Vas = new UserProfile("Vasya", "Vasya", "Vasya@gmail.com");
        Vas.setScore(14);
        FirstFour.add(Vas);
        accountService.addUser(Vas.getLogin(), Vas);
        UserProfile Van = new UserProfile("Vanya", "Vanya", "Vanya@gmail.com");
        Van.setScore(12);
        FirstFour.add(Van);
        accountService.addUser(Van.getLogin(), Van);
        UserProfile Pet = new UserProfile("Petya", "Petya", "Petya@gmail.com");
        Pet.setScore(10);
        FirstFour.add(Pet);
        accountService.addUser(Pet.getLogin(), Pet);
        UserProfile Dan = new UserProfile("Danya", "Danya", "Danya@gmail.com");
        Dan.setScore(2);
        accountService.addUser(Dan.getLogin(), Dan);

        TreeSet<UserProfile> resultTree = accountService.getFirstPlayersByScore(4 - 1);

        assertEquals("GetFirstByScore", FirstFour, resultTree);
    }
}