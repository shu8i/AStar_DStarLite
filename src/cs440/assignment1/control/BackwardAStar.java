package cs440.assignment1.control;

import cs440.assignment1.model.Agent;
import cs440.assignment1.model.BinaryHeap;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Shahab Shekari on 10/3/14.
 */
public class BackwardAStar extends AStar {

    public BackwardAStar(Grid grid, Agent agent) {
        super(grid, agent);
    }

    public void search() {
        int counter = 0;
        while (!this.agent.position().equals(this.grid.getTargetPosition())) {
            this.agent.updateMemory();
            counter++;
            this.grid.getTargetPosition().setG(0).setS(counter);
            this.agent.position().setG(Integer.MAX_VALUE).setS(counter);

            this.open = new BinaryHeap(11, Block.Comparators.BY_F_VALUE);
            this.closed = new ArrayList<Block>();

            this.grid.getTargetPosition().setH(calculateHValue(this.grid.getTargetPosition()));
            this.open.add(this.grid.getTargetPosition());
            computePath(counter);

            while (!this.agent.position().equals(this.grid.getTargetPosition())) {
                try {
                    this.agent.move(this.agent.position().getPointer());
                } catch(IllegalArgumentException e) {
                    break;
                }
            }

            if (this.open.size() == 0) {
                System.out.println("Cannot reach the target :(");
                return;
            }
        }

        System.out.println("Reached Target :)");
    }

    protected int calculateHValue(Block block) {
        return Math.abs(block.coordinates().getX() - this.agent.position().coordinates().getX()) +
                Math.abs(block.coordinates().getY() - this.agent.position().coordinates().getY());
    }

    void computePath(int counter) {

        while (this.open.size() != 0 && this.agent.position().getG() > this.open.peek().getF()) {
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

}
