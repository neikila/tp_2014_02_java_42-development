package Interface;

import main.user.UserProfile;

import java.util.TreeSet;

public interface AccountService {

    public boolean addUser(String userName, UserProfile userProfile);

    public boolean addSessions(String sessionId, UserProfile userProfile);

    public int getAmountOfSessions();

    public int getAmountOfSessionsWitUserAsKey();

    public int getAmountOfUsers();

    public boolean isSessionWithSuchLoginExist(String userName);

    public UserProfile getUser(String userName);

    public UserProfile getSessions(String sessionId);

    public UserProfile getSessionsByLogin(String login);

    public void removeSession(String sessionId);

    public void createAdmin();

    public TreeSet <UserProfile> getFirstPlayersByScore(int limit);
}
