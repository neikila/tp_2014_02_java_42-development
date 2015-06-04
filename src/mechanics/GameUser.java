package mechanics;

import main.user.UserProfile;
import utils.Id;
import utils.Point;

/**
 * @author v.chibrikov
 */
public class GameUser {
    private Id <GameUser> id;
    private int myScore = 0;
    private int position = 0; // It is needed for left 1 or right 2 position on the screen
    private UserProfile user;

    private int health;
    private Point coordinate;

    public GameUser(Id <GameUser> id, UserProfile user) {
        this.id = id;
        this.user = user;
        this.health = 100;
    }

    public void setMyPosition(int myPos) {
        if (position == 0) {
            position = myPos;
            if (myPos == 1) {
                coordinate = new Point(32, 380);
            } else {
                coordinate = new Point(768, 380);
            }
        }
    }

    public int getMyPosition() { return position; }

    public int getMyScore() {
        return myScore;
    }

    public UserProfile getUser() { return user; }

    public Id <GameUser> getId() { return id; }

    public int getHealth() { return health; }

    public int reduceHealth(int damage) {
        health -= damage;
        return health;
    }

    public Point getCoordinate() { return coordinate; }

    public void incrementMyScore() {
        myScore++;
    }
}
