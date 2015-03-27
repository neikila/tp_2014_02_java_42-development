package main;

import java.util.*;

public class AccountService {
    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();
    private Map<String, UserProfile> sessionsWithUserAsKey = new HashMap<>();

    public AccountService() {
        //TODO Добавлять админа
/*        String login = "admin";
        String password = "admin";
        String server = "10";
        String email = "admin@gmail.com";
        UserProfile profile = new UserProfile(login, password, email, server);
        profile.setAdmin(true);
        profile.setScore(1000);
        addUser(login, profile);*/
    }


        public boolean addUser(String userName, UserProfile userProfile) {
        return (!users.containsKey(userName) && (users.put(userName, userProfile) == null));
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


    public int getAmountOfUsers() {return users.size();}

    public boolean isSessionWithSuchLoginExist(String userName) {

        return sessionsWithUserAsKey.containsKey(userName);
    }

    public UserProfile getUser(String userName) {
        return users.get(userName);
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

    public void preparationForTest() {
        // Создание в базе пользователей по дефолту. Имитация бд ввиду её отстсвия.
        String login = "admin";
        String password = "admin";
        String server = "10";
        String email = "admin@gmail.com";
        UserProfile profile = new UserProfile(login, password, email, server);
        profile.setAdmin(true);
        profile.setScore(1000);
        addUser(login, profile);

        login = "test";
        password = "test";
        server = "10";
        email = "test@gmail.com";
        profile = new UserProfile(login, password, email, server);
        profile.setScore(100);
        addUser(login, profile);
    }
}
