package solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import maze.MazeGenerator;

public class Solution {
    public ArrayList<ArrayList<Integer>> maze = MazeGenerator.mazeMatrix;
    private Map<ArrayList<Integer>, Integer> node = new HashMap<ArrayList<Integer>, Integer>(); // stored as location, color
    private Map<ArrayList<Integer>, String> state = new HashMap<ArrayList<Integer>, String>(); // stored as location; visited, processed
    private Map<ArrayList<Integer>, ArrayList<Integer>> predecessor = new HashMap<ArrayList<Integer>, ArrayList<Integer>>(); // stored as location, location
    private Stack<ArrayList<Integer>> stack = new Stack<>();
    
    // // Arraylist update () {} -- every change made is put in an arraylist to send to vis
    // // x y color
    // // update frame by frame and not all at once
    // // x andd y are ints, and multiple need to be returned at a time, as well s a color

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

    private ArrayList<Integer> findEnd() {
        int colNum = 0;
        ArrayList<Integer> endLocation = new ArrayList<>();
        while (endLocation.isEmpty() && colNum < maze.size()) {
            if (maze.get(colNum).indexOf(3) != -1) { // end is in this column
                endLocation.add(colNum);
                endLocation.add(maze.get(colNum).indexOf(3));
            }
            colNum++;
        }
        return endLocation;
    }

    int[][] dirList = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};    

    private void populateState() {
        for (int j = 0; j < maze.size(); j++) {
            for (int i = 0; i < maze.get(j).size(); i++) {
                if (maze.get(j).get(i) == 1 || maze.get(j).get(i) == 2 || maze.get(j).get(i) == 3) {
                    ArrayList<Integer> node = new ArrayList<>();
                    node.add(j);
                    node.add(i);
                    state.put(node, "undiscovered");
                }
            }
        }
        System.out.println(state);
    }

    // public ArrayList<ArrayList<Integer>> DFS() {
    //     findOnes();
    //     currentLocation = findStart();
    //     node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //     state.put(currentLocation, "visited");
    //     stack.push(currentLocation);
    //     ArrayList<Integer> nilList = new ArrayList<>();
    //     nilList.add(-1);
    //     nilList.add(-1);
    //     predecessor.put(currentLocation, nilList);
    //     // while end node not in node hashmap
    //         // does currentLocation have an unvisited right path?
    //             // yes: set child to be currentlocation 
    //         // else does currentLocation have an unvisited down path?
    //             // yes: set child to be currentLocation
    //         // else does currentLocation have an unvisited left path?
    //             // yes: set child to be currentLocation
    //         // else does currentLocation have an unvisited up path?
    //             // yes: set child to be currentLocation
    //         // else:
    //             // pop currentLocation
    //             // next top is currentLocation
    //     int i = 0;
    //     while (!node.containsKey(3) && i < 8) {
    //         if (hasRightPath(currentLocation)) {
    //             node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //             state.put(currentLocation, "visited");
    //             stack.push(currentLocation);
    //             predecessor.put(currentLocation, stack.peek());
    //         } 
    //         if (hasDownPath(currentLocation)) {
    //             node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //             state.put(currentLocation, "visited");
    //             stack.push(currentLocation);
    //             predecessor.put(currentLocation, stack.peek());
    //         }
    //         if (hasLeftPath(currentLocation)) {
    //             node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //             state.put(currentLocation, "visited");
    //             stack.push(currentLocation);
    //             predecessor.put(currentLocation, stack.peek());
    //         }
    //         if (hasUpPath(currentLocation)) {
    //             node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //             state.put(currentLocation, "visited");
    //             stack.push(currentLocation);
    //             predecessor.put(currentLocation, stack.peek());
    //         } else {
    //             state.put(currentLocation, "processsed");
    //             currentLocation = stack.pop();
    //         }
    //         System.out.println("currentlocation " + currentLocation);
    //         System.out.println("node" + node + "\nstate" + state + "\n");
    //         i++;
    //     }
    
    // }

    private ArrayList<ArrayList<Integer>> findPath(ArrayList<Integer> location) {
        System.out.println("entered find path method");
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        ArrayList<Integer> current = location;
        // int i = 0;
        System.out.println("current is set to " + location);
        while (!path.contains(findStart())) { //&& i < 10) {
            System.out.println("comparing " + current + " and " + findStart());
            path.add(0, current);
            current = predecessor.get(current);
            // i++;
        }
        System.out.println("path found: " + path);
        return path;
    }

    private boolean isValidPath(ArrayList<Integer> location) {
        if (location.get(0) < 0 || location.get(0) >= maze.size()) {
            return false;
        }
        if (location.get(1) < 0 || location.get(1) >= maze.get(0).size()) {
            return false;
        }
        if (maze.get(location.get(0)).get(location.get(1)) == 3) {
            return true;
        }
        if (maze.get(location.get(0)).get(location.get(1)) != 1) {
            return false;
        }
        return true;
    }

    public void DFS() { 
        node.clear();
        state.clear();
        predecessor.clear();
        populateState();
        System.out.println("state populated");
        stack.push(findStart());
        // int isDone = 0; 
        boolean isDone = false;
        while(!stack.empty() && !isDone) {
            ArrayList<Integer> currentLocation = new ArrayList<>();
            System.out.println("popping " + stack.peek());
            currentLocation = stack.pop();
            state.put(currentLocation, "processed");
            node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
            System.out.println("comparing for " + currentLocation.get(0) + " " + currentLocation.get(1) + " and " + findEnd().get(0) + " " + findEnd().get(1));
            if(currentLocation.get(0) == findEnd().get(0) && currentLocation.get(1) == findEnd().get(1)) {
                System.out.println("maze solved");
                System.out.println("finding path using " + currentLocation);
                findPath(currentLocation);
                isDone = true;
            }
            for (int[] direction : dirList) {
                int newX = currentLocation.get(1) + direction[0];
                int newY = currentLocation.get(0) + direction[1];
                ArrayList<Integer> tempPath = new ArrayList<>();
                tempPath.add(newY);
                tempPath.add(newX);
                System.out.println("checking path for " + tempPath + "\t" + isValidPath(tempPath) + " " + state.get(tempPath));
                if(isValidPath(tempPath) && state.get(tempPath).compareTo("undiscovered") == 0) {
                    System.out.println("pushing " + tempPath + " onto stack and marking visited");
                    stack.push(tempPath);
                    state.put(tempPath, "visited");
                    predecessor.put(tempPath, currentLocation);
                }
            }
            System.out.println("stack " + stack + "\nnodes " + node + "\nstates " + state + "\npreds " + predecessor);
            System.out.println("done?: " + isDone + "\n----------");
            // isDone++;
        }
    }

    // private boolean hasRightPath(ArrayList<Integer> location) {
    //     ArrayList<Integer> rightPath = new ArrayList<>();
    //     rightPath.add(location.get(0) + 1);
    //     rightPath.add(location.get(1));
    //     if (isValidPath(rightPath)) {
    //         currentLocation = rightPath;
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    // private boolean hasDownPath(ArrayList<Integer> location) {
    //     ArrayList<Integer> downPath = new ArrayList<>();
    //     downPath.add(location.get(0) + 1);
    //     downPath.add(location.get(1));
    //     if (isValidPath(downPath)) {
    //         currentLocation = downPath;
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    // private boolean hasLeftPath(ArrayList<Integer> location) {
    //     ArrayList<Integer> leftPath = new ArrayList<>();
    //     leftPath.add(location.get(0) + 1);
    //     leftPath.add(location.get(1));
    //     if (isValidPath(leftPath)) {
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    // private boolean hasUpPath(ArrayList<Integer> location) {
    //     ArrayList<Integer> upPath = new ArrayList<>();
    //     upPath.add(location.get(0) + 1);
    //     upPath.add(location.get(1));
    //     if (isValidPath(upPath)) {
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    public ArrayList<Integer> update(){
    //     System.out.println(maze);
    //     findOnes();
    //     currentLocation = findStart();
    //     // 0 is col, 1 is row
    //     if (hasRightPath(currentLocation)) {
    //         System.out.println("right path\t" + maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //     } if (hasDownPath(currentLocation)) {
    //         System.out.println("down path\t" + maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //     } if (hasLeftPath(currentLocation)) {
    //         System.out.println("left path\t" + maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
    //     } if (hasUpPath(currentLocation)) {
    //         System.out.println("up path\t\t" + maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
        // }
        return null; // x y color
    }


}
