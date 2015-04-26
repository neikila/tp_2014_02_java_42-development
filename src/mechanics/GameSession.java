package mechanics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author v.chibrikov
 */
public class GameSession {
    private final long startTime;
    private final GameUser first;
    private final GameUser second;
    private int num;
    private int numAmount;
    private String winner;

    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(String user1, String user2, int numAmount) {
        startTime = new Date().getTime();
        GameUser gameUser1 = new GameUser(user1);
        gameUser1.setMyPosition(1);
        gameUser1.setEnemyName(user2);

        GameUser gameUser2 = new GameUser(user2);
        gameUser2.setMyPosition(2);
        gameUser2.setEnemyName(user1);

        users.put(user1, gameUser1);
        users.put(user2, gameUser2);

        Random rand = new Random();
        num = (int) Math.pow(10, numAmount) + rand.nextInt(9 * (int) Math.pow(10, numAmount) - 1);
        winner = null;

        this.first = gameUser1;
        this.second = gameUser2;
    }

    public GameUser getEnemy(String user) {
        String enemyName = users.get(user).getEnemyName();
        return users.get(enemyName);
    }

    public GameUser getSelf(String user) {
        return users.get(user);
    }

    public long getSessionTime(){
        return new Date().getTime() - startTime;
    }

    public GameUser getFirst() {
        return first;
    }

    public GameUser getSecond() {
        return second;
    }

    public String getSequence() { return String.valueOf(num); }

    public int getWinner() {
        if (winner == null) {
            return 0;
        } else {
            if (winner.equals(first.getMyName())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public  boolean isCorrect(String login, String answer){
        if (answer.equals(String.valueOf(num))) {
            if (winner == null) {
                winner = login;
            }
            return true;
        } else {
            return false;
        }
    }
}
