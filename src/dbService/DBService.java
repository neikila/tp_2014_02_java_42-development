package dbService;

import main.user.UserProfile;

import java.util.List;

public interface DBService {
    String getLocalStatus();

    void save(UserProfile dataSet);

    void update(UserProfile dataSet);

    UserProfile readUser(long id);

    UserProfile readUserByName(String name);

    List<UserProfile> readAll();

    long countAllUsers();

    void deleteAllUsers();

    List<UserProfile> readLimitOrder(int limit);

    void shutdown();
}
