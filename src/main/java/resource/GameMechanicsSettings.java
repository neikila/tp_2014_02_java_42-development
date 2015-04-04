package resource;

import Interface.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 03.04.15.
 */
public class GameMechanicsSettings  implements Serializable, Resource {
    final private int timeLimit;
    private int numAmount;

    public GameMechanicsSettings() {
        timeLimit = 15;
        numAmount = 6;
    }

    public int getTimeLimit() { return timeLimit; }

    public int getNumAmount() { return numAmount; }

    public void checkState() {
        if (numAmount < 6)
            numAmount = 6;
    }
}
