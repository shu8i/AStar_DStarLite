package cs440.assignment1.control;

import cs440.assignment1.model.*;

import java.util.*;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class DStarLite extends AStar {

    public Integer k_m;

    public DStarLite(Grid grid, Agent agent) {
        super(grid, agent);
        this.open = new BinaryHeap(11, Block.Comparators.BY_KEY);
        k_m = 0;
        this.grid.getTargetPosition().setRHS(0);
        calculateKey(this.grid.getTargetPosition());
        this.open.add(this.grid.getTargetPosition());
    }

    public boolean search() {

        computePath();

        Block last = this.grid.getStartingPosition();

        while (!this.grid.getStartingPosition().equals(this.grid.getTargetPosition())) {

            if (this.grid.getStartingPosition().getG() == Integer.MAX_VALUE) {
                return false;
            }

            Block next = getMinSucc(this.grid.getStartingPosition());
            try {
                this.agent.move(next);
                this.grid.setStartingPosition(next);
            } catch(IllegalArgumentException e) {
                next.setC(Integer.MAX_VALUE);
                k_m += calculateHValue(this.grid.getStartingPosition(), last);
                last = this.grid.getStartingPosition();
                updateVertex(this.grid.getStartingPosition());
                computePath();
                continue;
            }
        }

        return true;
    }

    private Block getMinSucc(Block block) {
        List<Block> neighbors = getSucc(block);
        int min = Integer.MAX_VALUE, curr;
        Block next = block;

        for (final Block b : neighbors) {
            if (b.getG() == Integer.MAX_VALUE || b.getC() == Integer.MAX_VALUE) {
                curr = Integer.MAX_VALUE;
            } else {
                curr = b.getG() + b.getC();
            }

            if (curr < min) {
                min = curr;
                next = b;
            }
        }

        if (next == block) {
            for (final Block b : neighbors) {
                if (b.getC() != Integer.MAX_VALUE) {
                    return b;
                }
            }
        }

        return next == block ? null : next;
    }

    private Block calculateKey(Block block) {
        int val = Math.min(block.getRHS(), block.getG());
        if (val == Integer.MAX_VALUE) {
            return block.setKey(new Key(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } else {
            return block.setKey(new Key(val + calculateHValue(block) + k_m, val));
        }
    }

    private void computePath() {

        Key oldKey;
        Block minBlock;

        while (!this.open.isEmpty() &&
                (this.open.peek().getKey().lt(calculateKey(this.grid.getStartingPosition()).getKey())) ||
                this.grid.getStartingPosition().getRHS() != this.grid.getStartingPosition().getG()) {

            oldKey = this.open.peek().getKey();
            minBlock = this.open.poll();

            if (oldKey.lt(calculateKey(minBlock).getKey())) {
                this.open.add(minBlock);
            } else if (minBlock.getG() > minBlock.getRHS()) {
                minBlock.setG(minBlock.getRHS());
                List<Block> pred = getPred(minBlock);
                for (final Block block : pred) {
                    updateVertex(block);
                }
            } else {
                    minBlock.setG(Integer.MAX_VALUE);
                    List<Block> pred = getPred(minBlock);
                    for (final Block block : pred) {
                        updateVertex(block);
                    }
                    updateVertex(minBlock);
            }
        }
    }


    private void updateVertex(Block block) {

        if (!this.grid.getTargetPosition().equals(block)) {
            Block minSucc = getMinSucc(block);
            if (minSucc == null) {
                return;
            }
            if (minSucc.getG() == Integer.MAX_VALUE || minSucc.getC() == Integer.MAX_VALUE) {
                block.setRHS(Integer.MAX_VALUE);
            } else {
                block.setRHS(minSucc.getG() + minSucc.getC());
            }
        }

        if (this.open.contains(block)) {
            this.open.remove(block);
        }

        if (block.getG() != block.getRHS()) {
            calculateKey(block);
            this.open.add(block);
        }
    }

    private LinkedList<Block> getSucc(Block u)
    {
        LinkedList<Block> s = new LinkedList<Block>();
        Block tempBlock;

        if(this.agent.remembers(u)) return s;

        tempBlock = this.grid.getBlock(new Coordinate(u.coordinates().getX()+1, u.coordinates().getY()));
        if (tempBlock != null) s.add(tempBlock);

        tempBlock = this.grid.getBlock(new Coordinate(u.coordinates().getX()-1, u.coordinates().getY()));
        if (tempBlock != null) s.add(tempBlock);

        tempBlock = this.grid.getBlock(new Coordinate(u.coordinates().getX(), u.coordinates().getY()+1));
        if (tempBlock != null) s.add(tempBlock);

        tempBlock = this.grid.getBlock(new Coordinate(u.coordinates().getX(), u.coordinates().getY()-1));
        if (tempBlock != null) s.add(tempBlock);

        return s;
    }


    private List<Block> getPred(Block block) {


        Coordinate currentCoordinate = block.coordinates();
        List<Block> validMoves = new ArrayList<Block>();
        List<Coordinate> possibleMoves = new ArrayList<Coordinate>();

        possibleMoves.add(new Coordinate(currentCoordinate.getX()+1, currentCoordinate.getY()));
        possibleMoves.add(new Coordinate(currentCoordinate.getX()-1, currentCoordinate.getY()));
        possibleMoves.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()+1));
        possibleMoves.add(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()-1));

        for (final Coordinate coordinate : possibleMoves) {
//            if (this.agent.wouldMoveBeValid(block.coordinates(), coordinate)) {
            if (this.grid.isCoordinateWithinBounds(coordinate) &&
                    !this.agent.remembers(this.grid.getBlock(coordinate))) {
                validMoves.add(this.grid.getBlock(coordinate));
            }
        }

        return validMoves;
    }

    protected int calculateHValue(Block block) {
        return Math.abs(block.coordinates().getX() - this.grid.getStartingPosition().coordinates().getX()) +
                Math.abs(block.coordinates().getY() - this.grid.getStartingPosition().coordinates().getY());
    }

    protected int calculateHValue(Block block1, Block block2) {
        return Math.abs(block1.coordinates().getX() - block2.coordinates().getX()) +
                Math.abs(block1.coordinates().getY() - block2.coordinates().getY());
    }

}