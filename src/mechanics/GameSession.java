package mechanics;

import utils.Id;

import java.util.Date;

public class GameSession {
    private final long startTime;
    private final GameUser first;
    private final GameUser second;
    private GameMap map;
    private GameResult winner;
    private State state = State.Playing;

    private int countStep = -1;

    private enum State {
        Playing,
        Finished
    }

    public GameSession(GameUser first, GameUser second, GameMap map) {
        this.map = map;
        startTime = new Date().getTime();
        winner = null;

        this.first = first;
        this.second = second;
    }

    public GameUser getEnemy(int myPosition) {
        if (myPosition == 1)
            return second;
        else
            return first;
    }

    public GameUser getSelf(int myPosition) {
        if (myPosition == 1)
            return first;
        else
            return second;
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

    public int getCountStep() {
        return countStep;
    }

    public void counterToZero() {
        countStep = 0;
    }

    public int makeStep() {
        return ++countStep;
    }

    public GameResult getWinner() {

        if (first.getHealth() > second.getHealth())
            winner = GameResult.FirstWon;
        if (second.getHealth() > first.getHealth())
            winner = GameResult.SecondWon;
        if (first.getHealth() == second.getHealth())
            winner = GameResult.Draw;

        return winner;
    }

    public GameUser getUser(Id <GameUser> id) {
        if (first.getId().getId() == id.getId()) {
            return first;
        } else {
            return second;
        }
    }

    public GameMap getMap() { return map; }

    public boolean isFinished() { return state == State.Finished; }

    public void finish() { state = State.Finished; }
}
