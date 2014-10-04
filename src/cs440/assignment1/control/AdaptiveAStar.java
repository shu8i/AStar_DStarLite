package cs440.assignment1.control;

import cs440.assignment1.model.Agent;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Grid;

import java.util.List;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class AdaptiveAStar extends ForwardAStar {

    public AdaptiveAStar(Grid grid, Agent agent) {
        super(grid, agent);
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

        int goalG = this.grid.getTargetPosition().getG();
        for (final Block move : this.closed) {
            move.setH(goalG - move.getG());
        }

    }

}
