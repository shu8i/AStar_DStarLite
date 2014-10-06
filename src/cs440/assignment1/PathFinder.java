package cs440.assignment1;

import cs440.assignment1.control.*;
import cs440.assignment1.model.Agent;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Grid;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class PathFinder {

    public static void main(String[] args) {
        compareAStarAlgorithms();
//        doDStar();
    }

    private static void compareAStarAlgorithms() {
        final int sampleSize = 50;

        int adaptiveAStarExpandedBlocks = 0,
                adaptiveAStarSuccesses = 0,
                forwardAStarExpandedBlocks = 0,
                forwardAStarSuccesses = 0,
                forwardAStarExpandedBlocks2 = 0,
                forwardAStarSuccesses2 = 0,
                backwardAStarExpandedBlocks = 0,
                backwardAStarSuccesses = 0;

        Grid grid, gridCopy, gridCopy2, gridCopy3;
        Agent agent, agentCopy, agentCopy2, agentCopy3;
        AStar aStar;

        int count = 0;
        while (count++ < sampleSize) {
            grid = new Grid.Builder().build();
            gridCopy = new Grid(grid);
            gridCopy2 = new Grid(grid);
            gridCopy3 = new Grid(grid);
            agent = new Agent(grid);
            agentCopy = new Agent(gridCopy);
            agentCopy2 = new Agent(gridCopy2);
            agentCopy3 = new Agent(gridCopy3);

            aStar = new AdaptiveAStar(grid, agent);
            adaptiveAStarSuccesses += aStar.search() ? 1 : 0;
            adaptiveAStarExpandedBlocks += aStar.getNumExpandedBlocks();

            aStar = new ForwardAStar(gridCopy, agentCopy, Block.Comparators.BY_F_LARGER_G);
            forwardAStarSuccesses += aStar.search() ? 1 : 0;
            forwardAStarExpandedBlocks += aStar.getNumExpandedBlocks();

            aStar = new ForwardAStar(gridCopy2, agentCopy2, Block.Comparators.BY_F_SMALLER_G);
            forwardAStarSuccesses2 += aStar.search() ? 1 : 0;
            forwardAStarExpandedBlocks2 += aStar.getNumExpandedBlocks();

            aStar = new BackwardAStar(gridCopy3, agentCopy3);
            backwardAStarSuccesses += aStar.search() ? 1 : 0;
            backwardAStarExpandedBlocks += aStar.getNumExpandedBlocks();

        }

        System.out.println("The average expansion for Adaptive A* is "
                + (adaptiveAStarExpandedBlocks / sampleSize) +
                " (" + adaptiveAStarSuccesses + "/" + sampleSize + " paths found).");
        System.out.println("The average expansion for Forward A* is "
                + (forwardAStarExpandedBlocks / sampleSize) +
                " (" + forwardAStarSuccesses + "/" + sampleSize + " paths found).");
        System.out.println("The average expansion for Forward A* Reverse is "
                + (forwardAStarExpandedBlocks2 / sampleSize) +
                " (" + forwardAStarSuccesses2 + "/" + sampleSize + " paths found).");
        System.out.println("The average expansion for Backward A* is "
                + (backwardAStarExpandedBlocks / sampleSize) +
                " (" + backwardAStarSuccesses + "/" + sampleSize + " paths found).");
    }

    private static void doDStar() {

        Grid grid = new Grid.Builder().build(),
             gridCopy = new Grid(grid);
        Agent agent = new Agent(grid),
              agentCopy = new Agent(gridCopy);

        DStarLite dStar = new DStarLite(grid, agent);
        AdaptiveAStar aaStar = new AdaptiveAStar(gridCopy, agentCopy);

        long startTime = System.currentTimeMillis();
        dStar.search();
        long endTime = System.currentTimeMillis();
        System.out.println(grid + "\n" + (endTime-startTime));


        startTime = System.currentTimeMillis();
        aaStar.search();
        endTime = System.currentTimeMillis();
        System.out.println(gridCopy + "\n" + (endTime-startTime));

    }

}
