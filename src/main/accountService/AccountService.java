package main.accountService;

import main.user.UserProfile;

import java.util.List;

public interface AccountService {

    public boolean addUser(String userName, UserProfile userProfile);

    public boolean addSessions(String sessionId, UserProfile userProfile);

    public boolean addPhoneSession(String phoneSessionId, String login);

    public void removePhoneSession(String login);

    public int getAmountOfSessions();

    public int getAmountOfSessionsWitUserAsKey();

    public long getAmountOfUsers();

    public boolean isSessionWithSuchLoginExist(String userName);

    public boolean isPhoneSessionWithSuchLoginExist(String userName);

    public UserProfile getUser(String userName);

    public UserProfile getSessions(String sessionId);

    public UserProfile getSessionsByLogin(String login);

    public UserProfile getUserFromPhoneSession(String phoneId);

    public void removeSession(String sessionId);

    public void createAdmin();

    public void createTestAccount();

    public void updateUser(UserProfile user);

    public List<UserProfile> getFirstPlayersByScore(int limit);
}
