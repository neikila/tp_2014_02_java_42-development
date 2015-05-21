package mechanics;

import main.user.UserProfile;
import utils.Id;

/**
 * @author v.chibrikov
 */
public class GameUser {
    private Id <GameUser> id;
    private int myScore = 0;
    private int position; // It is needed for left or right position on the screen
    private UserProfile user;

    public GameUser(Id <GameUser> id, UserProfile user) {
        this.id = id;
        this.user = user;
    }

    public void setMyPosition(int myPos) { position = myPos; }

    public int getMyPosition() { return position; }

    public int getMyScore() {
        return myScore;
    }

    public UserProfile getUser() { return user; }

    public Id <GameUser> getId() { return id; }

    public void incrementMyScore() {
        myScore++;
    }
}
