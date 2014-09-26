package cs440.assignment1.model;

import static cs440.assignment1.model.BlockState.*;

/**
 * Created by Shahab Shekari on 9/25/14.
 */
public class Agent {

    private Block position;
    private Grid grid;

    public Agent(Grid grid) {
        this.grid = grid;
        this.position = grid.getStartingPosition();
        this.position.add(AGENT);
    }

    public Block position() {
        return this.position;
    }

    public Agent move(Block newBlock) {
        if (!isMoveValid(newBlock.coordinates())) {
            throw new IllegalArgumentException("New coordinates are invalid!");
        }

        this.position.remove(AGENT).add(BREADCRUMB);
        this.position = newBlock;
        this.position.add(AGENT);

        return this;
    }

    public boolean isMoveValid(Coordinate coordinate) {
        return this.grid.isCoordinateValid(coordinate)
                && Math.abs(coordinate.getX() - this.position.coordinates().getX()) <= 1
                && Math.abs(coordinate.getY() - this.position.coordinates().getY()) <= 1;
    }

    public boolean isMoveValid(Coordinate oldCoordinate, Coordinate newCoordinate) {
        return this.grid.isCoordinateValid(newCoordinate)
                && Math.abs(newCoordinate.getX() - oldCoordinate.getX()) <= 1
                && Math.abs(newCoordinate.getY() - oldCoordinate.getY()) <= 1;
    }

}
