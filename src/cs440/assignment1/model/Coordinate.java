package cs440.assignment1.model;

/**
 * Created by Shahab Shekari on 9/24/14.
 */
public class Coordinate {

    private int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }

}
