package main.accountService;

import main.Context;
import main.user.UserComparatorByScore;
import main.user.UserProfile;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

import java.util.*;

public class AccountServiceImpl implements AccountService, Abonent, Runnable  {
    final private Map<String, UserProfile> users = new HashMap<>();
    final private Map<String, UserProfile> sessions = new HashMap<>();
    final private Map<String, UserProfile> sessionsWithUserAsKey = new HashMap<>();

    final private MessageSystem messageSystem;
    final private Address address;

    public AccountServiceImpl(Context context) {
        address = new Address();
        messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
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


    public long getAmountOfUsers() {return (long)users.size();}

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

    public List <UserProfile> getFirstPlayersByScore(int limit) {
        UserComparatorByScore comp = new UserComparatorByScore();
        TreeSet <UserProfile> FirstFour = new TreeSet<>(comp);
        Collection<UserProfile> collection = users.values();
        Iterator<UserProfile> iterator = collection.iterator();
        UserProfile temp;

        while (iterator.hasNext()) {
            temp = iterator.next();
            if (FirstFour.size() < limit) {
                FirstFour.add(temp);
            } else {
                if (FirstFour.size() > 0 && comp.compare(FirstFour.last(), temp) > 0) {
                    FirstFour.pollLast();
                    FirstFour.add(temp);
                }
            }
        }
        return new ArrayList<>(FirstFour);
    }

    public void updateUser(UserProfile userProfile) {
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Address getAddress (){
        return address;
    }
}