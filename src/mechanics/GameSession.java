package mechanics;

import utils.Id;

import java.util.Date;

public class GameSession {
    private final long startTime;
    private final GameUser first;
    private final GameUser second;
    private GameMap map;
    private Id<GameUser> id;

    public GameSession(GameUser first, GameUser second, GameMap map) {
        this.map = map;
        startTime = new Date().getTime();
        id = null;

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

    public int getWinner() {
        if (id == null) {
            return 0;
        } else {
            if (id.equals(first.getId())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public GameMap getMap() { return map; }
}
