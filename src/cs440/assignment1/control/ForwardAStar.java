package cs440.assignment1.control;

import cs440.assignment1.model.Agent;
import cs440.assignment1.model.BinaryHeap;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Grid;

import java.util.*;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class ForwardAStar extends AStar {

    private Comparator<Block> comparator;

    public ForwardAStar(Grid grid, Agent agent, Comparator<Block> comparator) {
        super(grid, agent);
        this.comparator = comparator;
    }

    public boolean search() {

        int counter = 0;
        while (!this.agent.position().equals(this.grid.getTargetPosition())) {
            this.agent.updateMemory();
            counter++;
            this.agent.position().setG(0).setS(counter);
            this.grid.getTargetPosition().setG(Integer.MAX_VALUE).setS(counter);

            this.open = new BinaryHeap(11, comparator);
            this.closed = new ArrayList<Block>();

            this.agent.position().setH(calculateHValue(this.agent.position()));
            this.open.add(this.agent.position());
            computePath(counter);

            if (this.open.size() == 0) {
                return false;
            }

            Stack<Block> path = getPath();
            while (!path.isEmpty()) {
                try {
                    this.agent.move(path.pop());
                    this.numMoved++;
                } catch(IllegalArgumentException e) {
                    break;
                }
            }
        }

        return true;

    }

    private void computePath(int counter) {

        while (this.open.size() != 0 && this.grid.getTargetPosition().getG() > this.open.peek().getF()) {
            Block minBlock = this.open.poll();
            this.closed.add(minBlock);
            this.numExpandedBlocks++;
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

        while(!nextBlock.equals(this.agent.position())) {
            path.add(nextBlock);
            nextBlock = nextBlock.getPointer();
        }

        return path;
    }

    protected int calculateHValue(Block block) {
        return Math.abs(block.coordinates().getX() - this.grid.getTargetPosition().coordinates().getX()) +
                Math.abs(block.coordinates().getY() - this.grid.getTargetPosition().coordinates().getY());
    }

}
