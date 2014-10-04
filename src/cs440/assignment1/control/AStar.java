package cs440.assignment1.control;

import cs440.assignment1.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahab Shekari on 10/3/14.
 */
public abstract class AStar {

    protected Grid grid;
    protected Agent agent;
    protected BinaryHeap open;
    protected List<Block> closed;
    protected int numExpandedBlocks;

    public AStar(Grid grid, Agent agent) {
        this.grid = grid;
        this.agent = agent;
        this.open = new BinaryHeap(11, Block.Comparators.BY_F_VALUE);
        this.closed = new ArrayList<Block>();
        this.numExpandedBlocks = 0;
    }

    protected List<Block> getValidMoves(Block block) {
        Coordinate currentCoordinate = block.coordinates();
        List<Block> validMoves = new ArrayList<Block>();
        List<Coordinate> possibleMoves = new ArrayList<Coordinate>();

        possibleMoves.add(new Coordinate(currentCoordinate.getX()+1, currentCoordinate.getY()));
        possibleMoves.add(new Coordinate(currentCoordinate.getX()-1, currentCoordinate.getY()));
        possibleMoves.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()+1));
        possibleMoves.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()-1));

        for (final Coordinate coordinate : possibleMoves) {
            if (this.agent.wouldMoveBeValid(block.coordinates(), coordinate)) {
                validMoves.add(this.grid.getBlock(coordinate));
            }
        }

//        return validMoves.size() == 0 ? null : validMoves;
        return validMoves;
    }

    public int getNumExpandedBlocks() {
        return this.numExpandedBlocks;
    }

    public abstract void search();
    protected abstract void computePath(int counter);
    protected abstract int calculateHValue(Block block);

}
