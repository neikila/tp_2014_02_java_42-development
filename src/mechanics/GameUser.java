package mechanics;

import main.user.UserProfile;
import utils.Id;
import utils.Point;

import java.util.Date;

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

    private long whenWasPressed;
    private GameDirection direction = GameDirection.None;

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

    public GameDirection getDirection() { return direction; }

    public long getTime() { return whenWasPressed; }

    public void appendTime(long delta) { whenWasPressed += delta; }

    public void press(int dir) {
        GameDirection temp;
        switch (dir) {
            case 0: temp = GameDirection.None; break;
            case 1: temp = GameDirection.Up; break;
            case 2: temp = GameDirection.Right; break;
            case 3: temp = GameDirection.Down; break;
            case 4: temp = GameDirection.Left; break;
            default: temp = GameDirection.None;
        }
        if (temp != GameDirection.None) {
            whenWasPressed = new Date().getTime();
            direction = temp;
        }
    }

    public void release() {
        direction = GameDirection.None;
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
