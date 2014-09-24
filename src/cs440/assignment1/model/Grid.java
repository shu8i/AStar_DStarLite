package cs440.assignment1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import static cs440.assignment1.model.BlockState.*;

/**
 * Created by Shahab Shekari on 9/24/14.
 */
public class Grid {

    private static final int GRID_WIDTH = 101, GRID_HEIGHT = 101;
    private static final int GRID_SIZE = GRID_WIDTH * GRID_HEIGHT;
    private Block[][] grid;

    private Grid(Builder builder) {
        this.grid = builder.grid;
    }

    public static class Builder {

        private Block[][] grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        /**
         * Visited, free (unblocked) coordinates.
         * @see Coordinate
         */
        private Stack<Block> unblockedPath = new Stack<Block>();
        private Random random = new Random();

        public Builder() {
            initializeEmptyGrid();
        }

        public Grid build() {

            Block currentBlock = this.grid[random.nextInt(GRID_HEIGHT)][random.nextInt(GRID_WIDTH)];
            currentBlock.remove(UNVISITED).add(VISITED).add(FREE);
            unblockedPath.add(currentBlock);
            int numVisitedBlocks = 1;

            while (numVisitedBlocks < GRID_SIZE) {

                while ( (currentBlock = getNextBlock(currentBlock)) == null ) {
                    currentBlock = unblockedPath.pop();
                }

                if (random.nextInt(10) > 6) {
                    currentBlock.add(BLOCKED);
                } else {
                    currentBlock.add(FREE);
                }

                currentBlock.remove(UNVISITED).add(VISITED);
                unblockedPath.add(currentBlock);
                numVisitedBlocks++;

            }

            return new Grid(this);

        }

        private Block getNextBlock(Block currentBlock) {

            List<Coordinate> validCoordinates = new ArrayList<Coordinate>();
            Coordinate currentCoorinates = currentBlock.coordinates();

            if (coordinateIsValid(currentCoorinates.getX()+1, currentCoorinates.getY())) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX()+1, currentCoorinates.getY()));
            }

            if (coordinateIsValid(currentCoorinates.getX()-1, currentCoorinates.getY())) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX()-1, currentCoorinates.getY()));
            }

            if (coordinateIsValid(currentCoorinates.getX(), currentCoorinates.getY()+1)) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX(), currentCoorinates.getY()+1));
            }

            if (coordinateIsValid(currentCoorinates.getX(), currentCoorinates.getY()-1)) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX(), currentCoorinates.getY()-1));
            }

            if (validCoordinates.size() == 0) {
                return null;
            } else {
                Coordinate nextCoordinate = validCoordinates.get(random.nextInt(validCoordinates.size()));
                return this.grid[nextCoordinate.getY()][nextCoordinate.getX()];
            }
        }

        private boolean coordinateIsValid(int x, int y) {

            if (y >= GRID_HEIGHT || y < 0 || x >= GRID_WIDTH || x < 0) {
                return false;
            }

            Block block = this.grid[y][x];
            return block.is(UNVISITED);
        }

        private void initializeEmptyGrid() {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j] = new Block(j, i);
                }
            }
        }


    }

}
