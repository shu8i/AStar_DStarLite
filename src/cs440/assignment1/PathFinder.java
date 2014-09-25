package cs440.assignment1;

import cs440.assignment1.model.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahab Shekari on 9/24/14.
 */
public class PathFinder {

    private static List<Grid> grids = new ArrayList<Grid>();

    public static void main(String[] args) {

        for(int i = 0; i < 50; i++) {
            grids.add(new Grid.Builder().build());
        }
        System.out.println(grids.get(0));
    }

}
