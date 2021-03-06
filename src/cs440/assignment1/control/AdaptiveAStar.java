package cs440.assignment1.control;

import cs440.assignment1.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class AdaptiveAStar extends AStar {

    public AdaptiveAStar(Grid grid, Agent agent) {
        super(grid, agent);
    }


    public boolean search() {

        int counter = 0;
        while (!this.agent.position().equals(this.grid.getTargetPosition())) {
            this.agent.updateMemory();
            counter++;
            this.agent.position().setG(0).setS(counter);
            this.grid.getTargetPosition().setG(Integer.MAX_VALUE).setS(counter);

            this.open = new BinaryHeap(11, Block.Comparators.BY_F_LARGER_G);
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
                } catch(IllegalArgumentException e) {
                    break;
                }
            }
        }

        return true;

    }

    protected void computePath(int counter) {

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

                    if (validMove.getH() == null) {
                        validMove.setH(calculateHValue(validMove));
                    }
                    this.open.add(validMove);
                }
            }

        }

        int before, after;
        int goalG = this.grid.getTargetPosition().getG();
        for (final Block move : this.closed) {
            before = move.getH();
            after = goalG - move.getG();

            if (before <= after) {
                move.setH(goalG - move.getG());
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
