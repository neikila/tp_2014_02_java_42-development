package mechanics;

/**
 * @author v.chibrikov
 */
public class GameUser {
    private int id;
    private final String myName;
    private int myScore = 0;
    private int position; // It is needed for left or right position on the screen

    public GameUser(String myName) {
        this.myName = myName;
    }

    public void setMyPosition(int myPos) { position = myPos; }

    public int getMyPosition() { return position; }

    public String getMyName() {
        return myName;
    }

    public int getMyScore() {
        return myScore;
    }

    public void incrementMyScore() {
        myScore++;
    }
}
