package main;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountService {
    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        return (!users.containsKey(userName) && (users.put(userName, userProfile) == null));
    }

    public void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public int getAmountOfSessions() {return sessions.size();}

    public int getAmountOfUsers() {return  users.size();}

    public boolean isSessionWithSuchLoginExist(String userName) {

        Collection<UserProfile> temp = sessions.values();
        Iterator<UserProfile> itr = temp.iterator();

        boolean returnValue = false;
        while (!returnValue && itr.hasNext()) {
            if (itr.next().getLogin().equals(userName)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
