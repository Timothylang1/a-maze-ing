package maze;

import java.util.ArrayList;

public class MazeGenerator {
    ArrayList<ArrayList<Integer>> mazeMatrix;
    ArrayList<Integer> yCoordinates;

    public MazeGenerator() {
        mazeMatrix = new ArrayList<ArrayList<Integer>>();
        yCoordinates = new ArrayList<Integer>();
    }

    public void setMazeSize(int x, int y) {
        // Create a matrix of size x and y. All values set to zero at first because everything is a wall.
        // The matrix will have the coordinate system (x, y)
        mazeMatrix.clear();
        for (int xCoord = 0; xCoord < x; xCoord++){
            mazeMatrix.add(new ArrayList<Integer>());
            for (int yCoord = 0; yCoord < y; yCoord++) {
                mazeMatrix.get(xCoord).add(0);
            }
        }
        
        System.out.println(mazeMatrix);
    }

    public ArrayList<Integer> update() {
        // Whatever block I'm updating, I should return that block. 
        // The resulting return value will contain 3 values:
        // The first value is an x-value
        // The second value is an y-value
        // The third value is a Color Code.

        mazeMatrix.get(0).set(0, 2);
        ArrayList<Integer> triple = new ArrayList<Integer>();
        triple.add(0);
        triple.add(0);
        triple.add(2);
        return triple;

    }
}
