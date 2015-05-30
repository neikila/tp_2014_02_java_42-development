package main.accountService;

import dbService.DBService;
import main.Context;
import main.user.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountServiceMySQLImpl implements AccountService {
    final private DBService dbService;

//    final private Map<String, UserProfile> sessions = new ConcurrentHashMap<>();
//    final private Map<String, UserProfile> sessionsWithUserAsKey = new ConcurrentHashMap<>();
//    final private Map<String, String> phoneSessionToLogin = new ConcurrentHashMap<>();
//    final private Map<String, String> phoneLoginToSession = new ConcurrentHashMap<>();

    final private Map<String, UserProfile> sessions = new HashMap<>();
    final private Map<String, UserProfile> sessionsWithUserAsKey = new HashMap<>();
    final private Map<String, String> phoneSessionToLogin = new HashMap<>();
    final private Map<String, String> phoneLoginToSession = new HashMap<>();

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

    public boolean addPhoneSession(String phoneSessionId, String login) {
        if (!phoneSessionToLogin.containsKey(phoneSessionId)) {
            phoneSessionToLogin.put(phoneSessionId, login);
            phoneLoginToSession.put(login, phoneSessionId);
            return true;
        } else {
            return false;
        }
    }

    public void removePhoneSession(String login) {
        phoneSessionToLogin.remove(phoneLoginToSession.get(login));
        phoneLoginToSession.remove(login);
    }

    public boolean isPhoneSessionWithSuchLoginExist(String userName) {

        return phoneLoginToSession.containsKey(userName);
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

    public UserProfile getUser(long id) {
        return dbService.readUser(id);
    }

    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public UserProfile getSessionsByLogin(String login) {
        return sessionsWithUserAsKey.get(login);
    }

    public UserProfile getUserFromPhoneSession(String phoneId) {
        return sessionsWithUserAsKey.get(phoneSessionToLogin.get(phoneId));
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

    public void updateUser(UserProfile userProfile) {
        dbService.update(userProfile);
    }
}