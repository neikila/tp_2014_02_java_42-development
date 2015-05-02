package main;

import Interface.DBService;
import main.user.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountServiceMySQLImpl implements Interface.AccountService{
    final private DBService dbService;
    final private Map<String, UserProfile> sessions = new HashMap<>();
    final private Map<String, UserProfile> sessionsWithUserAsKey = new HashMap<>();

    public AccountServiceMySQLImpl(Context context) {
        dbService = (DBService) context.get(DBService.class);
    }

    public boolean addUser(String userName, UserProfile userProfile) {
        if (dbService.readUserByName(userName) != null)
            return false;
        dbService.save(userProfile);
        return true;
    }

    public boolean addSessions(String sessionId, UserProfile userProfile) {
        if (!sessions.containsKey(sessionId)) {
            sessions.put(sessionId, userProfile);
            sessionsWithUserAsKey.put(userProfile.getLogin(), userProfile);
            return true;
        } else {
            return false;
        }
    }

    public int getAmountOfSessions() {return sessions.size();}

    public int getAmountOfSessionsWitUserAsKey() {return sessionsWithUserAsKey.size();}

    public long getAmountOfUsers() {return dbService.countAllUsers();}

    public boolean isSessionWithSuchLoginExist(String userName) {
        return sessionsWithUserAsKey.containsKey(userName);
    }

    public UserProfile getUser(String userName) {
        return dbService.readUserByName(userName);
    }

    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public UserProfile getSessionsByLogin(String login) {
        return sessionsWithUserAsKey.get(login);
    }

    public void removeSession(String sessionId) {
        sessionsWithUserAsKey.remove(sessions.get(sessionId).getLogin());
        sessions.remove(sessionId);
    }

    public void createAdmin() {
        // Создание в базе пользователей по дефолту. Имитация бд ввиду её отстсвия.
        String login = "admin";
        String password = "admin";
        String email = "admin@gmail.com";
        UserProfile profile = new UserProfile(login, password, email);
        profile.setAdmin(true);
        profile.setScore(1000);
        addUser(login, profile);
    }

    public void createTestAccount() {
        // Создание в базе пользователей по дефолту. Имитация бд ввиду её отстсвия.
        String login = "qwerty";
        String password = "qwerty";
        String email = "qwerty@mail.ru";
        UserProfile profile = new UserProfile(login, password, email);
        profile.setAdmin(false);
        profile.setScore(500);
        addUser(login, profile);
    }

    public List<UserProfile> getFirstPlayersByScore(int limit) {
        return dbService.readLimitOrder(limit);
    }
}