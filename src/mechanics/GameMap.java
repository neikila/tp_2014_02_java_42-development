package mechanics;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by neikila on 09.05.15.
 */
public class GameMap {

    private ArrayList<ArrayList<Integer>> map = new ArrayList<>();

    public GameMap (String fileName) throws IOException, WrongMapException {
        File file = new File("data/game/maps/" + fileName);

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), "UTF-8"
                )
        );

        ArrayList<Integer> temp;
        String line;
        int character;

        while ((line = br.readLine()) != null) {

            temp = new ArrayList<>();

            for (int i = 0; i < line.length(); ++i) {
                character = line.charAt(i) - '0';
                if (character > 1 || character < 0)
                    throw new WrongMapException("Wrong map file");
                temp.add(i, character);
            }

            map.add(temp);
        }
        br.close();

        if (map.size() == 0)
            throw new WrongMapException("Empty map file");
    }

    public int getElementType(int i, int j) {
        return map.get(i).get(j);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < map.size() - 1; ++i) {
            result
                    .append("{\"height\":").append(map.size() - i - 1)
                    .append(",\"values\":").append(map.get(i).toString()).append("},");
        }
        result
                .append("{\"height\":").append(0)
                .append(",\"values\":").append(map.get(map.size() - 1).toString()).append("}");
        result.append(']');
        return result.toString();
    }
}
