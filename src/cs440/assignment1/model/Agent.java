package cs440.assignment1.model;

import java.util.ArrayList;
import java.util.List;

import static cs440.assignment1.model.BlockState.*;

/**
 * Created by Shahab Shekari on 9/25/14.
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
        if (!canMove(newBlock.coordinates())) {
            this.memory.add(newBlock.coordinates());
            throw new IllegalArgumentException("New coordinates are invalid!");
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
        Coordinate coordinate = this.position.coordinates();
        if (!canMove(new Coordinate(coordinate.getX()+1, coordinate.getY()))) {
            if(!this.memory.contains(new Coordinate(coordinate.getX()+1, coordinate.getY()))) {
                this.memory.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
            }
        }
        if (!canMove(new Coordinate(coordinate.getX()-1, coordinate.getY()))) {
            if(!this.memory.contains(new Coordinate(coordinate.getX()-1, coordinate.getY()))) {
                this.memory.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
            }
        }
        if (!canMove(new Coordinate(coordinate.getX(), coordinate.getY()+1))) {
            if(!this.memory.contains(new Coordinate(coordinate.getX(), coordinate.getY()+1))) {
                this.memory.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
            }
        }
        if (!canMove(new Coordinate(coordinate.getX(), coordinate.getY()-1))) {
            if(!this.memory.contains(new Coordinate(coordinate.getX(), coordinate.getY()-1))) {
                this.memory.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
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
