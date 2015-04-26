package mechanics;

/**
 * @author v.chibrikov
 */
public class GameUser {
    private final String myName;
    private String enemyName;
    private int myScore = 0;
    private int enemyScore = 0;
    private int position; // It is needed for left or right position on the screen

    public GameUser(String myName) {
        this.myName = myName;
    }

    public void setMyPosition(int myPos) { position = myPos; }

    public int getMyPosition() { return position; }

    public String getMyName() {
        return myName;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getMyScore() {
        return myScore;
    }

    public int getEnemyScore() {
        return enemyScore;
    }

    public void incrementMyScore() {
        myScore++;
    }

    public void incrementEnemyScore() {
        enemyScore++;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }
}
