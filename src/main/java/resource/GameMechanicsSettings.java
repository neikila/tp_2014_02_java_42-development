package resource;

import Interface.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 03.04.15.
 */
public class GameMechanicsSettings  implements Serializable, Resource {
    private int timeLimit;

    public GameMechanicsSettings() { timeLimit = 15; }

    public int getTimeLimit() { return timeLimit; }
}
