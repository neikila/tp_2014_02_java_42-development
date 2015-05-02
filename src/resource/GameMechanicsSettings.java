package resource;

import java.io.Serializable;

/**
 * Created by neikila on 03.04.15.
 */
public class GameMechanicsSettings  implements Serializable, Resource {
    private int timeLimit;
    private int numAmount;
    private int weight;
    private int minDelta;

    public GameMechanicsSettings() {
        timeLimit = 15;
        numAmount = 6;
        weight = 10;
        minDelta = 5;
    }

    public int getTimeLimit() { return timeLimit; }

    public int getNumAmount() { return numAmount; }

    public int getWeight() { return weight; }

    public int getMinDelta() { return minDelta; }

    public void checkState() {
        if (numAmount < 6)
            numAmount = 6;
        if (numAmount > 9)
            numAmount = 9;
        if (timeLimit < 0)
            timeLimit = 0;
        if (weight < 0)
            weight = 0;
        if (minDelta < 0)
            minDelta = 0;
    }
}
