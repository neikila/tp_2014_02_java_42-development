package main;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {
    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userProfile);
        return true;
    }

    public boolean isUserExist(String userName) {
        if (users.containsKey(userName))
            return true;
        return false;
    }

    public void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public int getAmountOfSessions() {return  sessions.size();};

    public boolean isSessionWithSuchLoginExist(String userName) {
        Collection<UserProfile> temp = sessions.values();
        Iterator<UserProfile> itr = temp.iterator();
        while ( itr.hasNext() )
        {
            if ( itr.next().getLogin().equals(userName) )
            {
                return true;
            }
        }
        return false;
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
