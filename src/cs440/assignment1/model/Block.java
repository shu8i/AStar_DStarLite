package cs440.assignment1.model;

import cs440.assignment1.control.dstarlite.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static cs440.assignment1.model.BlockState.*;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class Block {

    private List<BlockState> blockState;
    private Integer g = Integer.MAX_VALUE, h, f, s, rhs = g;
    private Coordinate coordinate;
    private Block pointer;
    private Key key;

    public Block(int x, int y) {
        this.blockState = new ArrayList<BlockState>();
        this.blockState.add(BlockState.UNVISITED);
        this.coordinate = new Coordinate(x, y);
        this.s = 0;
        this.key = new Key(0, 0);
    }

    public boolean is(BlockState state) {
        return this.blockState.contains(state);
    }

    public Coordinate coordinates() {
        return this.coordinate;
    }

    public Block add(BlockState state) {
        this.blockState.add(state);
        return this;
    }

    public Block remove(BlockState state) {
        this.blockState.remove(state);
        return this;
    }

    public Block setG(int g) {
        this.g = g;
        updateF();
        return this;
    }

    public Block setH(int h) {
        this.h = h;
        updateF();
        return this;
    }

    public Block setS(int s) {
        this.s = s;
        return this;
    }

    public Block setRHS(int rhs) {
        this.rhs = rhs;
        return this;
    }

    public Block setKey(Key key) {
        this.key = key;
        return this;
    }

    public Block setPointer(Block block) {
        this.pointer = block;

        remove(TOP).remove(BOTTOM).remove(LEFT).remove(RIGHT);

        if(this.pointer.coordinates().getX() > this.coordinates().getX()) {
            add(RIGHT);
        } else if (this.pointer.coordinates().getX() < this.coordinates().getX()) {
            add(LEFT);
        } else if (this.pointer.coordinates().getY() > this.coordinates().getY()) {
            add(BOTTOM);
        } else {
            add(TOP);
        }

        return this;
    }

    public Integer getG() {
        return this.g;
    }

    public Integer getH() {
        return this.h;
    }

    public Integer getF() {
        return this.f;
    }

    public Integer getS() {
        return this.s;
    }

    public Integer getRHS() {
        return this.rhs;
    }

    public Key getKey() {
        return this.key;
    }

    public Block getPointer() {
        return this.pointer;
    }

    private void updateF() {
        if(this.g != null && this.h != null) {
            this.f = this.g + this.h;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Block block = (Block) o;
        return block.coordinates() != null &&
                block.coordinates().equals(this.coordinates());
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        for(final BlockState state : this.blockState) {
            stringBuffer.append(state + ", ");
        }

        return stringBuffer.substring(0, stringBuffer.length()-2);
    }

    public static class Comparators {

        public static Comparator<Block> BY_F_LARGER_G = new Comparator<Block>() {
            @Override
            public int compare(Block block1, Block block2) {
                Random random = new Random();
                if (block1.getF() == block2.getF()) {
                    if (block1.getG() == block2.getG()) {
                        return random.nextInt(2) == 0 ? -1 : 1;
                    } else {
                        return block2.getG() - block1.getG();
                    }
                }
                return block1.getF() - block2.getF();
            }
        };

        public static Comparator<Block> BY_F_SMALLER_G = new Comparator<Block>() {
            @Override
            public int compare(Block block1, Block block2) {
                Random random = new Random();
                if (block1.getF() == block2.getF()) {
                    if (block1.getG() == block2.getG()) {
                        return random.nextInt(2) == 0 ? -1 : 1;
                    } else {
                        return block1.getG() - block2.getG();
                    }
                }
                return block1.getF() - block2.getF();
            }
        };

        public static Comparator<Block> BY_KEY = new Comparator<Block>() {
            @Override
            public int compare(Block block1, Block block2) {
                return block1.getKey().lt(block2.getKey()) ? -1 :
                        (block1.getKey().gt(block2.getKey()) ? 1 : 0);
            }
        };

    }

}
