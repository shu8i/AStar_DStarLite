package cs440.assignment1;

import cs440.assignment1.control.AStar;
import cs440.assignment1.control.BackwardAStar;
import cs440.assignment1.control.ForwardAStar;
import cs440.assignment1.model.Agent;
import cs440.assignment1.model.Block;
import cs440.assignment1.model.Coordinate;
import cs440.assignment1.model.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahab Shekari on 9/24/14.
 */
public class PathFinder {

    public static void main(String[] args) {

        Grid grid = new Grid.Builder().build(),
             gridCopy = new Grid(grid);
        Agent agent = new Agent(grid),
              agentCopy = new Agent(gridCopy);
        System.out.println(grid);
        AStar forwardAStar = new ForwardAStar(grid, agent),
              backwardAStar = new BackwardAStar(gridCopy, agentCopy);
        forwardAStar.search();
        backwardAStar.search();
        System.out.println("Forward A*:\n" + grid);
        System.out.println("Backward A*:\n" + gridCopy);

    }

}
