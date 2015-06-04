package utils;

/**
 * Created by neikila on 04.06.15.
 */
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setXY(double x, double y) {
        this.x = x; this.y = y;
    }
}
