package cs440.assignment1.control;

import cs440.assignment1.model.*;
import static cs440.assignment1.model.BlockState.*;
import static cs440.assignment1.model.Block.*;

import java.util.*;

/**
 * Created by Shahab Shekari on 10/5/14.
 */
public class DStarLite extends AStar {

//    public Map<Block, Block> blockHash = new HashMap<Block, Block>();       //TODO fix this
//    public List<Block> blockList = new ArrayList<Block>();
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

        while (!this.grid.getStartingPosition().equals(this.grid.getTargetPosition())) {
            this.agent.updateMemory();
            computePath();
            if (this.grid.getStartingPosition().getG() == Integer.MAX_VALUE) {
                return false;
            }

            try {
                this.agent.move(getMinSucc(this.grid.getStartingPosition()));
                this.grid.setStartingPosition(getMinSucc(this.grid.getStartingPosition()));
            } catch(IllegalArgumentException e) {
                continue;
            }
            System.out.println(grid);
            System.out.println();
        }

        return true;
    }

    private Block getMinSucc(Block block) {
        List<Block> neighbors = getSucc(this.grid.getStartingPosition());
        int min = Integer.MAX_VALUE;
        Block next = this.grid.getStartingPosition();

        for (final Block b : neighbors) {
            if (b.getG() < min) {
                min = b.getG();
                next = b;
            }
        }
        return next;
    }

    private Block calculateKey(Block block) {
        int val = Math.min(block.getRHS(), block.getG());
        return block.setKey(new Key(val + calculateHValue(block) + k_m, val));
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
        List<Block> neighbors;

        if (!this.grid.getTargetPosition().equals(block)) {
            neighbors = getSucc(block);
            int tmp = Integer.MAX_VALUE;
            int tmp2;

            for (final Block b : neighbors) {
                if (b.getG() == Integer.MAX_VALUE) {
                    continue;
                }
                tmp2 = b.getG() + 1;
                if (tmp2 < tmp) tmp = tmp2;
            }

            block.setRHS(tmp);
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

}