package mechanics;

/**
 * Created by neikila on 24.05.15.
 */
public enum GameDirection {
    Up, Right, Down, Left, None;

    public static GameDirection getDirection(int num) {
        switch (num) {
            case 0: return GameDirection.Up;
            case 1: return GameDirection.Right;
            case 2: return GameDirection.Down;
            case 3: return GameDirection.Left;
            default: return GameDirection.None;
        }
    }
}
