package Test;

import main.AccountService;
import main.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// TODO Нужно проверить редирректы? какие редирректы?

public class AccountServiceTest {
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
        assertEquals(0, accountService.getAmountOfUsers());
    }

    @After
    public void tearDown() throws Exception {
        accountService = null;
        // TODO как удалить accountService?
        //accountService.
    }

    @Test
    public void testAddUser() throws Exception {

        //TODO Проверкаа корректности ввода лучше делать внутри accountService?
        // Если да, то можно ли пренебречь информативностью ответа в случае ошибки?

        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");

        accountService.addUser("test1234", user);

        assertEquals(1, accountService.getAmountOfUsers());

        accountService.addUser("test1234", user);

        assertEquals(1, accountService.getAmountOfUsers());
        //TODO Спросить: нужно делать два теста, если я хочу проверить разные поведения ОДНОЙ функции
        //TODO Спросить нужно ли сделать try catch
    }

    @Test
    public void testAddSessions() throws Exception {

        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addSessions("test1234", user);
        user = new UserProfile("test1235", "test1235", "neikila12@gmail.com", "10");
        accountService.addSessions("test1235", user);

        assertEquals(accountService.getAmountOfSessions(), 2);
        assertEquals(accountService.getAmountOfSessionsWitUserAsKey(), 2);

        user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addSessions("test1234", user);
        user = new UserProfile("test1235", "test1235", "neikila12@gmail.com", "10");
        accountService.addSessions("test1235", user);

        assertEquals(accountService.getAmountOfSessions(), 2);
        assertEquals(accountService.getAmountOfSessionsWitUserAsKey(), 2);
    }

    //TODO Тесты выполняются ассинхронно?

    @Test
    public void testGetAmountOfSessions() throws Exception {
//TODO Проверить путем вызывается ли или не проверять?
    }

    @Test
    public void testGetAmountOfUsers() throws Exception {
// см. выше
    }

    @Test
    public void testIsSessionWithSuchLoginExist() throws Exception {
        //System.out.println();
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addSessions("test1234", user);

        boolean resultTrue = accountService.isSessionWithSuchLoginExist("test1234");
        boolean resultFalse = accountService.isSessionWithSuchLoginExist("test1235");

        assertEquals(resultTrue, true);
        assertEquals(resultFalse, false);

        //System.out.println("AccountService: IsSessionWithSuchLoginExist : Success!");

        //TODO для проверки приватных методов необходимо класс сделать дружественным или же сделать функцию паблик (О_о)
    }

    @Test
    public void testGetUser() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addUser("test1234", user);

        UserProfile resultUserTrue = accountService.getUser("test1234");
        UserProfile resultUserFalse = accountService.getUser("test1235");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);

        //System.out.println("AccountService: GetUser : Success!");
    }

    @Test
    public void testGetSessions() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addSessions("111111", user);

        UserProfile resultUserTrue = accountService.getSessions("111111");
        UserProfile resultUserFalse = accountService.getSessions("121212");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testGetSessionsByLogin() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addSessions("111111", user);

        UserProfile resultUserTrue = accountService.getSessionsByLogin("test1234");
        UserProfile resultUserFalse = accountService.getSessionsByLogin("test1235");

        assertEquals(resultUserTrue, user);
        assertNotEquals(resultUserFalse, user);
    }

    @Test
    public void testRemoveSession() throws Exception {
        UserProfile user = new UserProfile("test1234", "test1234", "neikila@gmail.com", "10");
        accountService.addSessions("111111", user);

        accountService.removeSession("111111");

        assertEquals(accountService.getAmountOfSessions(), 0);
        assertEquals(accountService.getAmountOfSessionsWitUserAsKey(), 0);
    }

    @Test
    public void testPreparationForTest() throws Exception {
        String login = "admin";
        String password = "admin";
        String server = "10";
        String email = "admin@gmail.com";
        UserProfile profileAdmin = new UserProfile(login, password, email, server);
        profileAdmin.setAdmin(true);
        profileAdmin.setScore(1000);

        login = "test";
        password = "test";
        server = "10";
        email = "test@gmail.com";
        UserProfile profileTest = new UserProfile(login, password, email, server);
        profileTest.setScore(100);


        accountService.preparationForTest();

        UserProfile resultUserAdmin = accountService.getUser("admin");
        UserProfile resultUserTest = accountService.getUser("test");

        assertEquals(resultUserAdmin.getLogin(), profileAdmin.getLogin());
        assertEquals(resultUserAdmin.getEmail(), profileAdmin.getEmail());
        assertEquals(resultUserAdmin.getPassword(), profileAdmin.getPassword());
        assertEquals(resultUserAdmin.getRole(), profileAdmin.getRole());
        assertEquals(resultUserAdmin.getServer(), profileAdmin.getServer());
        assertEquals(resultUserAdmin.isAdmin(), profileAdmin.isAdmin());
        assertEquals(resultUserAdmin.isAdmin(), true);


        assertEquals(resultUserTest.getLogin(), profileTest.getLogin());
        assertEquals(resultUserTest.getEmail(), profileTest.getEmail());
        assertEquals(resultUserTest.getPassword(), profileTest.getPassword());
        assertEquals(resultUserTest.getRole(), profileTest.getRole());
        assertEquals(resultUserTest.getServer(), profileTest.getServer());
        assertEquals(resultUserTest.getScore(), profileTest.getScore());
        assertEquals(resultUserTest.isAdmin(), profileTest.isAdmin());
        assertEquals(resultUserTest.isAdmin(), false);


        //assertEquals(resultUserTest, profileTest);
        //TODO как проверять сложные объекты можно ли где-то для них перегрузить метод equals?
    }
}

// TODO Нужно ли проверять подгружаемые из OpenSourse библиотеки? Проверка email например