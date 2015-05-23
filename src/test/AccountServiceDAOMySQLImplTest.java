package test;

import main.accountService.AccountServiceDAO;
import dbService.DBService;
import dbService.DBServiceImpl;
import main.accountService.AccountServiceDAOMySQLImpl;
import main.Context;
import main.user.UserComparatorByScore;
import main.user.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resource.DbServerSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AccountServiceDAOMySQLImplTest {
    AccountServiceDAO accountServiceDAO;
    DBService dbService;

    @Before
    public void setUp() throws Exception {
        DbServerSettings dbServerSettings = mock(DbServerSettings.class);
        when(dbServerSettings.getConnectionUrl()).thenReturn("jdbc:mysql://localhost:3306/gameTestDB");
        when(dbServerSettings.getDialect()).thenReturn("org.hibernate.dialect.MySQLDialect");
        when(dbServerSettings.getDriverClass()).thenReturn("com.mysql.jdbc.Driver");
        when(dbServerSettings.getMode()).thenReturn("validate");
        when(dbServerSettings.getShowSql()).thenReturn("false");
        when(dbServerSettings.getPassword()).thenReturn("rfrltkf");
        when(dbServerSettings.getUsername()).thenReturn("gameAdmin");

        dbService = new DBServiceImpl(dbServerSettings);
        Context context = new Context();
        context.add(DBService.class, dbService);
        accountServiceDAO = new AccountServiceDAOMySQLImpl(context);
    }

    @After
    public void tearDown() throws Exception {
        dbService.deleteAllUsers();
    }

    @Test
    public void testAddUser() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountServiceDAO.addUser("test1234", user);

        assertEquals(1, accountServiceDAO.getAmountOfUsers());
    }

    @Test
    public void testCreateTest() throws Exception {
        String login = "qwerty";
        String password = "qwerty";
        String email = "qwerty@mail.ru";
        UserProfile profile = new UserProfile(login, password, email);
        profile.setAdmin(false);
        profile.setScore(500);

        accountServiceDAO.createTestAccount();

        assertEquals(profile, accountServiceDAO.getUser(login));
    }

    @Test
    public void testAddUserIfAlreadyExist() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountServiceDAO.addUser("test1234", user);
        accountServiceDAO.addUser("test1234", user);

        assertEquals(1, accountServiceDAO.getAmountOfUsers());
    }


    @Test
    public void testAddSessions() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountServiceDAO.addSessions("test1234", user);

        assertEquals(accountServiceDAO.getAmountOfSessions(), 1);
        assertEquals(accountServiceDAO.getAmountOfSessionsWitUserAsKey(), 1);
    }

    @Test
    public void testAddSessionsIfAlreadyExist() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountServiceDAO.addSessions("test1234", user);
        accountServiceDAO.addSessions("test1234", user);

        assertEquals(accountServiceDAO.getAmountOfSessions(), 1);
        assertEquals(accountServiceDAO.getAmountOfSessionsWitUserAsKey(), 1);
    }

    @Test
    public void testGetAmountOfUsers() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        UserProfile user1 = new UserProfile("test12341", "test1234", "neikila1@gmail.com");

        accountServiceDAO.addUser("test1234", user);
        accountServiceDAO.addUser("test12341", user1);

        assertEquals(2, accountServiceDAO.getAmountOfUsers());
    }

    @Test
    public void testIsSessionWithSuchLoginExist() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountServiceDAO.addSessions("test1234", user);

        boolean resultTrue = accountServiceDAO.isSessionWithSuchLoginExist("test1234");
        boolean resultFalse = accountServiceDAO.isSessionWithSuchLoginExist("test1235");

        assertEquals(resultTrue, true);
        assertEquals(resultFalse, false);
    }

    @Test
    public void testGetUser() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountServiceDAO.addUser("test1234", user);

        UserProfile resultUserTrue = accountServiceDAO.getUser("test1234");
        UserProfile resultUserFalse = accountServiceDAO.getUser("test1235");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testGetSessions() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountServiceDAO.addSessions("111111", user);

        UserProfile resultUserTrue = accountServiceDAO.getSessions("111111");
        UserProfile resultUserFalse = accountServiceDAO.getSessions("121212");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testGetSessionsByLogin() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountServiceDAO.addSessions("111111", user);

        UserProfile resultUserTrue = accountServiceDAO.getSessionsByLogin("test1234");
        UserProfile resultUserFalse = accountServiceDAO.getSessionsByLogin("test1235");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testRemoveSession() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        accountServiceDAO.addSessions("111111", user);

        accountServiceDAO.removeSession("111111");

        assertEquals(accountServiceDAO.getAmountOfSessions(), 0);
        assertEquals(accountServiceDAO.getAmountOfSessionsWitUserAsKey(), 0);
    }

    @Test
    public void testCreateAdmin() throws Exception {
        String login = "admin";
        String password = "admin";
        String email = "admin@gmail.com";
        UserProfile profileAdmin = new UserProfile(login, password, email);
        profileAdmin.setAdmin(true);
        profileAdmin.setScore(1000);

        accountServiceDAO.createAdmin();

        UserProfile resultUserAdmin = accountServiceDAO.getUser("admin");

        assertEquals(resultUserAdmin, profileAdmin);
    }

    @Test
    public void testGetFirstByScore() throws Exception {
        UserComparatorByScore comp = new UserComparatorByScore();
        TreeSet<UserProfile> FirstFour = new TreeSet<>(comp);

        UserProfile Vas = new UserProfile("Vasya", "Vasya", "Vasya@gmail.com");
        Vas.setScore(14);
        FirstFour.add(Vas);
        accountServiceDAO.addUser(Vas.getLogin(), Vas);
        UserProfile Van = new UserProfile("Vanya", "Vanya", "Vanya@gmail.com");
        Van.setScore(12);
        FirstFour.add(Van);
        accountServiceDAO.addUser(Van.getLogin(), Van);
        UserProfile Pet = new UserProfile("Petya", "Petya", "Petya@gmail.com");
        Pet.setScore(10);
        FirstFour.add(Pet);
        accountServiceDAO.addUser(Pet.getLogin(), Pet);
        UserProfile Dan = new UserProfile("Danya", "Danya", "Danya@gmail.com");
        Dan.setScore(2);
        accountServiceDAO.addUser(Dan.getLogin(), Dan);
        FirstFour.add(Dan);

        List<UserProfile> test = new ArrayList<>(FirstFour);

        List<UserProfile> resultTree = accountServiceDAO.getFirstPlayersByScore(4);

        assertEquals("GetFirstByScore", test, resultTree);
    }
}