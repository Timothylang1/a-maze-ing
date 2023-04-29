package solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import maze.MazeGenerator;

public class Solution {
    public ArrayList<ArrayList<Integer>> maze = MazeGenerator.mazeMatrix;
    private Map<Integer, ArrayList<Integer>> node = new HashMap<Integer, ArrayList<Integer>>(); // stored as color, location
    private Map<ArrayList<Integer>, String> state = new HashMap<ArrayList<Integer>, String>(); // stored as location; unvisited, processed
    private Map<ArrayList<Integer>, ArrayList<Integer>> predecessor = new HashMap<ArrayList<Integer>, ArrayList<Integer>>(); // stored as location, location
    ArrayList<Integer> currentLocation = new ArrayList<>();
    
    // Arraylist update () {} -- every change made is put in an arraylist to send to vis
    // x y color
    // update frame by frame and not all at once
    // x andd y are ints, and multiple need to be returned at a time, as well s a color

    private ArrayList<Integer> findStart() {
        int colNum = 0;
        ArrayList<Integer> startLocation = new ArrayList<>();
        while (startLocation.isEmpty() && colNum < maze.size()) {
            if (maze.get(colNum).indexOf(2) != -1) { // start is in this column
                startLocation.add(colNum);
                startLocation.add(maze.get(colNum).indexOf(2));
            }
            colNum++;
        }
        return startLocation;
    }

    public ArrayList<ArrayList<Integer>> DFS() {
        currentLocation = findStart();
        // while end node not in node hashmap
            // does currentLocation have an unvisited right path?
                // yes: set child to be currentlocation 
            // else does currentLocation have an unvisited down path?
                // yes: set child to be currentLocation
            // else does currentLocation have an unvisited left path?
                // yes: set child to be currentLocation
            // else does currentLocation have an unvisited up path?
                // yes: set child to be currentLocation
            // else:
                // pop currentLocation
                // next top is currentLocation
        // while (!node.containsKey(3)) {
        //     if () {
        return maze;

        //     }
        // }
    }

    private boolean hasRightPath(ArrayList<Integer> location) {
        if (maze.get(location.get(0) + 1).get(location.get(1)) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasDownPath(ArrayList<Integer> location) {
        if (maze.get(location.get(0)).get(location.get(1) + 1) == 1) {
            return true;
        } else {
            return false;
        }
    }



    public ArrayList<Integer> update(){
        currentLocation = findStart();
        // 0 is col, 1 is row
        if (hasRightPath(currentLocation)) {
            System.out.println("right path\t" + (currentLocation.get(0) + 1) + " " + currentLocation.get(1));
            
        } if (hasDownPath(currentLocation)) {
            System.out.println("down path\t" + currentLocation.get(0) + " " + (currentLocation.get(1) + 1));
        } //if (maze.get(currentLocation.get(0) - 1).get(currentLocation.get(1)) == 1) {
        //     System.out.println("has left path and it is at " + (currentLocation.get(0) - 1) + " " + currentLocation.get(1));
        // } if (maze.get(currentLocation.get(0)).get(currentLocation.get(1) - 1) == 1) {
        //     System.out.println("has up path and it is at " + currentLocation.get(0) + " " + (currentLocation.get(1) - 1));
        // }
        return currentLocation;
    }


}
