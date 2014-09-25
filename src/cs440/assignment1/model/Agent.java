package cs440.assignment1.model;

import static cs440.assignment1.model.BlockState.*;

/**
 * Created by Shahab Shekari on 9/25/14.
 */
public class Agent {

    private Block currentPosition;
    private Grid grid;

    public Agent(Grid grid) {
        this.grid = grid;
        this.currentPosition = grid.getStartingPosition();
        this.currentPosition.add(AGENT);
    }

    public Block getCurrentPosition() {
        return this.currentPosition;
    }

    public Agent move(Coordinate newCoordinate) {
        if (!isMoveValid(newCoordinate)) {
            throw new IllegalArgumentException("New coordinates are invalid!");
        }

        this.currentPosition.remove(AGENT);
        this.currentPosition = this.grid.getBlock(newCoordinate);
        this.currentPosition.add(AGENT);

        return this;
    }

    private boolean isMoveValid(Coordinate coordinate) {
        return this.grid.isCoordinateValid(coordinate)
                && Math.abs(coordinate.getX() - this.currentPosition.coordinates().getX()) <= 1
                && Math.abs(coordinate.getY() - this.currentPosition.coordinates().getY()) <= 1;
    }

}
