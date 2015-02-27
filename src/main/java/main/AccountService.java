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

    // Метод получения количества авторизованных пользователей
    public int getAmountOfSessions() {return  sessions.size();}

    // Метод получения количества авторизованных пользователей
    public int getAmountOfUsers() {return  users.size();}

    // Поиск сессии по логину
    public boolean isSessionWithSuchLoginExist(String userName) {

        // Для перебор все элементов в карте необходимо выполнить преобразование к коллекции
        Collection<UserProfile> temp = sessions.values();
        // Создание итератора по коллекции
        Iterator<UserProfile> itr = temp.iterator();

        // Выполнение перебора
        boolean returnValue = false;
        while (!returnValue && itr.hasNext() )
        {
            if ( itr.next().getLogin().equals(userName) )
            {
                returnValue = true;
            }
        }
        return returnValue;
    }

    // Валидация UserName'а
    public boolean IsUserNameValid(String userName) {
        boolean returnValue = true;
        if (userName.length() < 5)
        {
            returnValue = false;
        }
        return returnValue;
    }


    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    // Удаление сессии по ключу
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
