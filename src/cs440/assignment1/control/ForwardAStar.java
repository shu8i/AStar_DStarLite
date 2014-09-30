package cs440.assignment1.control;

import cs440.assignment1.model.Agent;
import cs440.assignment1.model.BinaryHeap;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Coordinate;
import cs440.assignment1.model.Grid;

import java.util.*;

/**
 * Created by Shahab Shekari on 9/25/14.
 */
public class ForwardAStar {

    private Grid grid;
    private Agent agent;
    private Queue<Block> open;
    //private BinaryHeap open;
    private List<Block> closed;


    public ForwardAStar(Grid grid, Agent agent) {
        this.grid = grid;
        this.agent = agent;

        int counter = 0;
        while (!agent.position().equals(grid.getTargetPosition())) {
            counter++;
            this.agent.position().setG(0).setS(counter);
            this.grid.getTargetPosition().setG(Integer.MAX_VALUE).setS(counter);

            this.open = new PriorityQueue<Block>(11, Block.Comparators.BY_F_VALUE);
            //this.open = new BinaryHeap(11, Block.Comparators.BY_F_VALUE);
            this.closed = new ArrayList<Block>();

            this.agent.position().setH(calculateHValue(this.agent.position()));
            this.open.add(this.agent.position());
            computePath(counter);

            if (this.open.size() == 0) {
                System.out.println("Cannot reach the target :(");
                break;
            }

            Stack<Block> path = getPath();
            while (!path.isEmpty()) {
                this.agent.move(path.pop());
            }
            System.out.println("Reached Target :)");
        }

    }

    private void computePath(int counter) {

        while (this.open.size() != 0 && this.grid.getTargetPosition().getG() > this.open.peek().getF()) { //TODO understand why size check is necessary... for when stuck?
            Block minBlock = this.open.poll();
            this.closed.add(minBlock);
            List<Block> validMoves = getValidMoves(minBlock);

            for (final Block validMove : validMoves) {
                if (validMove.getS() < counter) {
                    validMove.setG(Integer.MAX_VALUE).setS(counter);
                }
                if (validMove.getG() > (minBlock.getG() + 1)) {
                    validMove.setG(minBlock.getG() + 1);
                    validMove.setPointer(minBlock);

                    if (this.open.contains(validMove)) {
                        this.open.remove(validMove);
                    }

                    validMove.setH(calculateHValue(validMove));
                    this.open.add(validMove);

                }
            }

        }

    }

    private Stack<Block> getPath() {
        Stack<Block> path = new Stack<Block>();
        path.add(this.grid.getTargetPosition());
        Block nextBlock = this.grid.getTargetPosition().getPointer();

        while(!nextBlock.equals(this.grid.getStartingPosition())) {
            path.add(nextBlock);
            nextBlock = nextBlock.getPointer();
        }

        return path;
    }

    private List<Block> getValidMoves(Block block) {
        Coordinate currentCoordinate = block.coordinates();
        List<Block> validMoves = new ArrayList<Block>();
        List<Coordinate> possibleMoves = new ArrayList<Coordinate>();

        possibleMoves.add(new Coordinate(currentCoordinate.getX()+1, currentCoordinate.getY()));
        possibleMoves.add(new Coordinate(currentCoordinate.getX()-1, currentCoordinate.getY()));
        possibleMoves.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()+1));
        possibleMoves.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()-1));

        for (final Coordinate coordinate : possibleMoves) {
            if (this.agent.isMoveValid(block.coordinates(), coordinate)) {
                validMoves.add(this.grid.getBlock(coordinate));
            }
        }

        return validMoves.size() == 0 ? null : validMoves;
    }

    private int calculateHValue(Block block) {
        return Math.abs(block.coordinates().getX() - this.grid.getTargetPosition().coordinates().getX()) +
                Math.abs(block.coordinates().getY() - this.grid.getTargetPosition().coordinates().getY());
    }

}
