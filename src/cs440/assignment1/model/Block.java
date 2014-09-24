package cs440.assignment1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahab Shekari on 9/24/14.
 */
public class Block {

    private List<BlockState> blockState;
    private int g, h, f;
    private Coordinate coordinate;

    public Block(int x, int y) {
        this.blockState = new ArrayList<BlockState>();
        this.blockState.add(BlockState.UNVISITED);
        this.coordinate = new Coordinate(x, y);
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

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        for(final BlockState state : this.blockState) {
            stringBuffer.append(state + ", ");
        }

        return stringBuffer.substring(0, stringBuffer.length()-2);
    }

}
