package cs440.assignment1;

import cs440.assignment1.control.AStar;
import cs440.assignment1.control.AdaptiveAStar;
import cs440.assignment1.control.BackwardAStar;
import cs440.assignment1.control.ForwardAStar;
import cs440.assignment1.model.Agent;
import cs440.assignment1.model.Grid;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class PathFinder {

    public static void main(String[] args) {
        compareAStarAlgorithms();
    }

    private static void compareAStarAlgorithms() {
        int numTests = 1000;

        int adaptiveAStarExpandedBlocks = 0,
                adaptiveAStarSuccesses = 0,
                forwardAStarExpandedBlocks = 0,
                forwardAStarSuccesses = 0,
                backwardAStarExpandedBlocks = 0,
                backwardAStarSuccesses = 0;
        Grid grid, gridCopy, gridCopy2;
        Agent agent, agentCopy, agentCopy2;
        AStar aStar;

        int count = 0;
        while (count++ < numTests) {
            grid = new Grid.Builder().build();
            gridCopy = new Grid(grid);
            gridCopy2 = new Grid(grid);
            agent = new Agent(grid);
            agentCopy = new Agent(gridCopy);
            agentCopy2 = new Agent(gridCopy2);

            aStar = new AdaptiveAStar(grid, agent);
            adaptiveAStarSuccesses += aStar.search() ? 1 : 0;
            adaptiveAStarExpandedBlocks += aStar.getNumExpandedBlocks();

            aStar = new ForwardAStar(gridCopy, agentCopy);
            forwardAStarSuccesses += aStar.search() ? 1 : 0;
            forwardAStarExpandedBlocks += aStar.getNumExpandedBlocks();

            aStar = new BackwardAStar(gridCopy2, agentCopy2);
            backwardAStarSuccesses += aStar.search() ? 1 : 0;
            backwardAStarExpandedBlocks += aStar.getNumExpandedBlocks();

        }

        System.out.println("The average expansion for Adaptive A* is "
                + adaptiveAStarExpandedBlocks / numTests +
                " (" + adaptiveAStarSuccesses + "/" + numTests + " paths found).");
        System.out.println("The average expansion for Forward A* is "
                + forwardAStarExpandedBlocks / numTests +
                " (" + forwardAStarSuccesses + "/" + numTests + " paths found).");
        System.out.println("The average expansion for Backward A* is "
                + backwardAStarExpandedBlocks / numTests +
                " (" + backwardAStarSuccesses + "/" + numTests + " paths found).");
    }

}
