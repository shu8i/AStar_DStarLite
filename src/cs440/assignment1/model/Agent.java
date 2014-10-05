package cs440.assignment1.model;

import java.util.ArrayList;
import java.util.List;

import static cs440.assignment1.model.BlockState.*;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class Agent {

    private Block position;
    private Grid grid;
    private List<Coordinate> memory;

    public Agent(Grid grid) {
        this.grid = grid;
        this.position = grid.getStartingPosition();
        this.memory = new ArrayList<Coordinate>();
        this.position.add(AGENT);
    }

    public Block position() {
        return this.position;
    }

    public Agent move(Block newBlock) {
        if (newBlock == null || !canMove(newBlock.coordinates())) {
            if (newBlock != null) {
                this.memory.add(newBlock.coordinates());
            }
//            throw new IllegalArgumentException("New coordinates are invalid!");
        }

        this.position.remove(AGENT).add(BREADCRUMB);
        this.position = newBlock;
        this.position.add(AGENT);
        return this;
    }

    public boolean canMove(Coordinate coordinate) {
        return this.grid.isCoordinateWithinBoundsAndUnblocked(coordinate);

    }

    public Agent updateMemory() {
        Coordinate currentCoordinate = this.position.coordinates();
        List<Coordinate> neighboringCoordinates = new ArrayList<Coordinate>();

        neighboringCoordinates.add(new Coordinate(currentCoordinate.getX()+1, currentCoordinate.getY()));
        neighboringCoordinates.add(new Coordinate(currentCoordinate.getX()-1, currentCoordinate.getY()));
        neighboringCoordinates.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()+1));
        neighboringCoordinates.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()-1));

        for (final Coordinate coordinate : neighboringCoordinates) {
            if (!canMove(coordinate) && !this.memory.contains(coordinate)) {
                this.memory.add(coordinate);
            }
        }

        return this;
    }

    public boolean wouldMoveBeValid(Coordinate oldCoordinate, Coordinate newCoordinate) {
        return this.grid.isCoordinateWithinBounds(newCoordinate)
                && !this.memory.contains(newCoordinate)
                && Math.abs(newCoordinate.getX() - oldCoordinate.getX()) <= 1
                && Math.abs(newCoordinate.getY() - oldCoordinate.getY()) <= 1;
    }

}
