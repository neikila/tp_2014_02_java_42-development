package resource;

import mechanics.GameMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by neikila on 03.04.15.
 */
public class GameMechanicsSettings  implements Serializable, Resource {

    private Logger logger = LogManager.getLogger(GameMechanicsSettings.class.getName());

    private int timeLimit;
    private int numAmount;
    private int weight;
    private int minDelta;
    private ArrayList<GameMap> maps;

    public GameMechanicsSettings() {
        timeLimit = 15;
        numAmount = 6;
        weight = 10;
        minDelta = 5;
        maps = new ArrayList<>();
    }

    public int getTimeLimit() { return timeLimit; }

    public int getNumAmount() { return numAmount; }

    public int getWeight() { return weight; }

    public int getMinDelta() { return minDelta; }

    public ArrayList<GameMap> getMaps() { return maps; }

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

        setAllMaps();
    }

    private void setAllMaps() {
        // TODO переделать в цикл
        try {
            GameMap temp = new GameMap("defaultMap");
            maps.add(temp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
