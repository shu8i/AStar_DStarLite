package cs440.assignment1.model;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
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
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Coordinate coordinate = (Coordinate) o;
        return coordinate.getX() == this.getX() &&
                coordinate.getY() == this.getY();
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }

}
