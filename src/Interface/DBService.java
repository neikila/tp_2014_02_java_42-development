package Interface;

import main.user.UserProfile;

import java.util.List;

/**
 * @author v.chibrikov
 */
public interface DBService {
    String getLocalStatus();

    void save(UserProfile dataSet);

    UserProfile readUser(long id);

    UserProfile readUserByName(String name);

    List<UserProfile> readAll();

    long countAllUsers();

    void vipe();

    void deleteAllUsers();

    List<UserProfile> readLimitOrder(int limit);

    void shutdown();
}
