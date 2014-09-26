package cs440.assignment1;

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

        Grid grid = new Grid.Builder().build();
        Agent agent = new Agent(grid);
        System.out.println(grid);
        ForwardAStar aStar = new ForwardAStar(grid, agent);
//        Block agentPosition = agent.getCurrentPosition();
//        agent.move(new Coordinate(agentPosition.coordinates().getX()+1, agentPosition.coordinates().getY())); //might throw exception if new position is blocked. For testing only.
        System.out.println(grid);

    }

}
