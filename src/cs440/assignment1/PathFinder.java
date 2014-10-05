package cs440.assignment1;

import cs440.assignment1.control.*;
import cs440.assignment1.model.Agent;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Grid;

import java.util.List;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class PathFinder {

    public static void main(String[] args) {
//        compareAStarAlgorithms();
        doDStar();
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
                backwardAStarSuccesses = 0,
                numMover = 0;
        Grid grid, gridCopy, gridCopy2, gridCopy3;
        Agent agent, agentCopy, agentCopy2, agentCopy3;
        AStar aStar;

        int count = 0;
        while (count++ < sampleSize) {
            grid = new Grid.Builder().build();
//            grid = new Grid(true);
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
//            System.out.println(grid);

            aStar = new ForwardAStar(gridCopy, agentCopy, Block.Comparators.BY_F_LARGER_G);
            forwardAStarSuccesses += aStar.search() ? 1 : 0;
            forwardAStarExpandedBlocks += aStar.getNumExpandedBlocks();
//            System.out.println(gridCopy);
//            System.out.println(aStar.getNumMoved());

            aStar = new ForwardAStar(gridCopy2, agentCopy2, Block.Comparators.BY_F_SMALLER_G);
            forwardAStarSuccesses2 += aStar.search() ? 1 : 0;
            forwardAStarExpandedBlocks2 += aStar.getNumExpandedBlocks();
//            System.out.println(gridCopy2);

            aStar = new BackwardAStar(gridCopy3, agentCopy3);
            backwardAStarSuccesses += aStar.search() ? 1 : 0;
            backwardAStarExpandedBlocks += aStar.getNumExpandedBlocks();
//            System.out.println(gridCopy3);

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
//        //Create pathfinder
//        DStarLite pf = new DStarLite();
//        //set start and goal nodes
//        pf.init();
//
//        //perform the pathfinding
//        pf.replan();
//
//        System.out.println(pf.getGrid());
//        //get and print the path
//        List<State> path = pf.getPath();
//        for (State i : path)
//        {
//            System.out.println("x: " + i.x + " y: " + i.y);
//        }

        Grid grid = new Grid.Builder().build();
        Agent agent = new Agent(grid);
        DStarLite dStar = new DStarLite(grid, agent);
        dStar.search();
        System.out.println(grid);

    }

}
