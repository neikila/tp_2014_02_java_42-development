package test;

import main.accountService.AccountService;
import dbService.DBService;
import dbService.DBServiceImpl;
import main.accountService.AccountServiceMySQLImpl;
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

public class AccountServiceMySQLImplTest {
    AccountService accountService;
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
        accountService = new AccountServiceMySQLImpl(context);
    }

    @After
    public void tearDown() throws Exception {
        dbService.deleteAllUsers();
    }

    @Test
    public void testAddUser() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");

        accountService.addUser("test1234", user);

        assertEquals(1, accountService.getAmountOfUsers());
    }

    @Test
    public void testCreateTest() throws Exception {
        String login = "qwerty";
        String password = "qwerty";
        String email = "qwerty@mail.ru";
        UserProfile profile = new UserProfile(login, password, email);
        profile.setAdmin(false);
        profile.setScore(500);

        accountService.createTestAccount();

        assertEquals(profile, accountService.getUser(login));
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
    public void testGetAmountOfUsers() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com");
        UserProfile user1 = new UserProfile("test12341", "test1234", "neikila1@gmail.com");

        accountService.addUser("test1234", user);
        accountService.addUser("test12341", user1);

        assertEquals(2, accountService.getAmountOfUsers());
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
        FirstFour.add(Dan);

        List<UserProfile> test = new ArrayList<>(FirstFour);

        List<UserProfile> resultTree = accountService.getFirstPlayersByScore(4);

        assertEquals("GetFirstByScore", test, resultTree);
    }
}