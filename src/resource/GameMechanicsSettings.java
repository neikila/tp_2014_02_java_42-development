package resource;

import mechanics.GameMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.vfs.VFS;
import utils.vfs.VFSImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by neikila on 03.04.15.
 */
public class GameMechanicsSettings  implements Serializable, Resource {

    private Logger logger = LogManager.getLogger(GameMechanicsSettings.class.getName());

    private int timeLimit;
    private int numAmount;
    private int weight;
    private int minDelta;
    private int syncFrequency;
    private int deltaPressed;
    private int damage;
    private ArrayList<GameMap> maps;

    public GameMechanicsSettings() {
        timeLimit = 15;
        numAmount = 6;
        weight = 10;
        minDelta = 5;
        syncFrequency = 500;
        deltaPressed = 20;
        damage = 10;
        maps = new ArrayList<>();
    }

    public int getSyncFrequency() { return syncFrequency; }

    public int getTimeLimit() { return timeLimit; }

    public int getNumAmount() { return numAmount; }

    public int getWeight() { return weight; }

    public int getMinDelta() { return minDelta; }

    public ArrayList<GameMap> getMaps() { return maps; }

    public long getDeltaPressed() { return (long)deltaPressed; }

    public int getDamage() { return damage; }

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
            VFS temp = new VFSImpl("data/game/");
            Iterator<String> iterator = temp.getIterator("maps/");
            while (iterator.hasNext()) {
                String fileName = iterator.next();
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                if (!temp.isDirectory(fileName)) {
                    GameMap map = new GameMap(fileName);
                    maps.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
