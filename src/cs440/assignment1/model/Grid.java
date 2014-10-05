package cs440.assignment1.model;

import cs440.assignment1.util.ExtendedAscii;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import static cs440.assignment1.model.BlockState.*;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class Grid {

    public static final int GRID_WIDTH = 5, GRID_HEIGHT = 5;
    public static final int GRID_SIZE = GRID_WIDTH * GRID_HEIGHT;
    private Block[][] grid;
    private Block startingPosition, targetPosition;

    public Grid(boolean lol) {
        this.grid = new Block[5][5];
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.grid[i][j] = new Block(j, i);
                this.grid[i][j].add(UNBLOCKED);
            }
        }
        this.startingPosition = this.grid[4][2];
        this.targetPosition = this.grid[4][4];
        this.startingPosition.add(START);
        this.targetPosition.add(TARGET);
        this.grid[2][2].remove(UNBLOCKED).add(BLOCKED);
        this.grid[3][2].remove(UNBLOCKED).add(BLOCKED);
        this.grid[4][3].remove(UNBLOCKED).add(BLOCKED);

    }

    public Grid(Grid grid) {
        cloneGrid(grid.grid);
        this.startingPosition = this.grid[grid.getStartingPosition().coordinates().getY()]
                [grid.getStartingPosition().coordinates().getX()];
        this.targetPosition = this.grid[grid.getTargetPosition().coordinates().getY()]
                [grid.getTargetPosition().coordinates().getX()];

        this.startingPosition.add(START);
        this.targetPosition.add(TARGET);
    }

    private Grid(Builder builder) {
        this.grid = builder.grid;
        this.startingPosition = builder.startingPosition;
        this.targetPosition = builder.targetPosition;
    }

    public Block getStartingPosition() {
        return this.startingPosition;
    }

    public Block getTargetPosition() {
        return this.targetPosition;
    }

    public Block setStartingPosition(Block block) {
        this.startingPosition = block;
        return this.startingPosition;
    }

    public Block setTargetPosition(Block block) {
        this.targetPosition = block;
        return this.targetPosition;
    }

    public Block getBlock(Coordinate coordinate) {
        return isCoordinateWithinBounds(coordinate) ?
                this.grid[coordinate.getY()][coordinate.getX()] :
                null;
    }

    public boolean isCoordinateWithinBoundsAndUnblocked(Coordinate coordinate) {

        if (coordinate.getY() >= GRID_HEIGHT || coordinate.getY() < 0 ||
                coordinate.getX() >= GRID_WIDTH || coordinate.getX() < 0) {
            return false;
        }

        Block block = this.grid[coordinate.getY()][coordinate.getX()];
        return block.is(UNBLOCKED);
    }

    public boolean isCoordinateWithinBounds(Coordinate coordinate) {
        return coordinate.getY() < GRID_HEIGHT && coordinate.getY() >= 0
                && coordinate.getX() < GRID_WIDTH && coordinate.getX() >= 0;
    }

    private void cloneGrid(Block[][] grid) {
        this.grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                this.grid[i][j] = new Block(j, i);
                this.grid[i][j].add(grid[i][j].is(BLOCKED) ? BLOCKED : UNBLOCKED);
            }
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.grid.length; i++) {
            stringBuilder.append("\t|");
            for (int j = 0; j < this.grid[0].length; j++) {
                Block block = this.grid[i][j];

                if (block.is(UNBLOCKED)) {
                    if(block.is(START)) {
                        stringBuilder.append("S");
                    } else if (block.is(AGENT)) {
                        stringBuilder.append("A");
                    } else if (block.is(TARGET)) {
                        stringBuilder.append("T");
                    } else if (block.is(BREADCRUMB)) {
                        stringBuilder.append(".");
                    }
                    else if (block.is(TOP)) {
                        stringBuilder.append("^");
                    } else if (block.is(BOTTOM)) {
                        stringBuilder.append("v");
                    } else if (block.is(LEFT)) {
                        stringBuilder.append("<");
                    } else if (block.is(RIGHT)) {
                        stringBuilder.append(">");
                    }
                    else {
                        stringBuilder.append(" ");
                    }
                } else {
                    stringBuilder.append(ExtendedAscii.getAscii(177));
                }

                stringBuilder.append("|");

            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static class Builder {

        private Block[][] grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        /**
         * Visited, free (unblocked) coordinates.
         * @see Coordinate
         */
        private Stack<Block> unblockedPath = new Stack<Block>();
        private Random random = new Random();
        private Block startingPosition, targetPosition;

        public Builder() {
            initializeEmptyGrid();
        }

        private void setEndpointPositions() {
            while ( (this.startingPosition = this.grid[random.nextInt(GRID_HEIGHT)][random.nextInt(GRID_WIDTH)]).is(BLOCKED) );
            while ( (this.targetPosition = this.grid[random.nextInt(GRID_HEIGHT)][random.nextInt(GRID_WIDTH)]).is(BLOCKED) );
            this.startingPosition.add(START);
            this.targetPosition.add(TARGET);
        }

        public Grid build() {

            Block currentBlock = this.grid[random.nextInt(GRID_HEIGHT)][random.nextInt(GRID_WIDTH)];
            currentBlock.remove(UNVISITED).add(VISITED).add(UNBLOCKED);
            unblockedPath.add(currentBlock);
            int numVisitedBlocks = 1;

            while (numVisitedBlocks < GRID_SIZE) {

                while ( (currentBlock = getNextBlock(currentBlock)) == null ) {
                    currentBlock = unblockedPath.pop();
                }

                if (random.nextInt(10) > 9) {
                    currentBlock.add(BLOCKED);
                } else {
                    currentBlock.add(UNBLOCKED);
                }

                currentBlock.remove(UNVISITED).add(VISITED);
                unblockedPath.add(currentBlock);
                numVisitedBlocks++;

            }

            setEndpointPositions();

            return new Grid(this);

        }

        private Block getNextBlock(Block currentBlock) {

            List<Coordinate> validCoordinates = new ArrayList<Coordinate>();
            Coordinate currentCoorinates = currentBlock.coordinates();

            if (isCoordinateValid(currentCoorinates.getX()+1, currentCoorinates.getY())) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX()+1, currentCoorinates.getY()));
            }

            if (isCoordinateValid(currentCoorinates.getX()-1, currentCoorinates.getY())) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX()-1, currentCoorinates.getY()));
            }

            if (isCoordinateValid(currentCoorinates.getX(), currentCoorinates.getY()+1)) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX(), currentCoorinates.getY()+1));
            }

            if (isCoordinateValid(currentCoorinates.getX(), currentCoorinates.getY()-1)) {
                validCoordinates.add(new Coordinate(currentCoorinates.getX(), currentCoorinates.getY()-1));
            }

            if (validCoordinates.size() == 0) {
                return null;
            } else {
                Coordinate nextCoordinate = validCoordinates.get(random.nextInt(validCoordinates.size()));
                return this.grid[nextCoordinate.getY()][nextCoordinate.getX()];
            }
        }

        private boolean isCoordinateValid(int x, int y) {

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
