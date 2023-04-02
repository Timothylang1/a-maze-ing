package maze;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

public class MazeGenerator {
    ArrayList<ArrayList<Integer>> mazeMatrix;
    int grid_size_x;
    int grid_size_y;
    Random randomGenerator;
    ArrayList<Integer> visualPathArray;
    ArrayList<Point> potentialPoints;

    public MazeGenerator() {
        mazeMatrix = new ArrayList<ArrayList<Integer>>();
        randomGenerator = new Random();
        potentialPoints = new ArrayList<Point>();
        visualPathArray = new ArrayList<Integer>();

        potentialPoints.add(new Point(0, 0));
    }

    public ArrayList<Integer> setMazeSize(int x, int y) {
        // Create a matrix of size x and y. All values set to zero at first because everything is a wall.
        // The matrix will have the coordinate system (x, y)
        mazeMatrix.clear();
        visualPathArray.clear();

        grid_size_x = x;
        grid_size_y = y;
        for (int xCoord = 0; xCoord < x; xCoord++){
            mazeMatrix.add(new ArrayList<Integer>());
            for (int yCoord = 0; yCoord < y; yCoord++) {
                mazeMatrix.get(xCoord).add(0);
            }
        }

        setMatrix(new Point(0, 0), 2);
        addToVisual(new Point(0, 0), 2);

        setMatrix(new Point(grid_size_x - 1, grid_size_y - 1), 3);
        addToVisual(new Point(grid_size_x - 1, grid_size_y - 1), 3);

        return visualPathArray;
    }

    public ArrayList<Integer> update() {
        // Whatever block I'm updating, I should return that block. 
        // The resulting return value will contain 3 values:
        // The first value is an x-value
        // The second value is an y-value
        // The third value is a Color Code.

        // Start @ (0, 0)
        mazeMatrix.get(0).set(0, 2);
        visualPathArray.add(0);
        visualPathArray.add(0);
        visualPathArray.add(2);

        // End @ (19, 19)
        mazeMatrix.get(grid_size_x-1).set(grid_size_y-1, 3);
        visualPathArray.add(grid_size_x-1);
        visualPathArray.add(grid_size_y-1);
        visualPathArray.add(3);
        // System.out.println(mazeMatrix);

        // Potential paths are 1 block away from a current block.
        // From the start, I can't go negative direction.

        // From (0, 0), my potential paths are (1, 0) or (0, 1), which means I'm moving in only one axis.
        Point endPoint = new Point(grid_size_x - 1, grid_size_y-1);

        ArrayList<Point> connectedPaths = new ArrayList<Point>();

        // Remove the chosenPoint from the potentialPaths list
        Point chosenPoint = potentialPoints.remove(randomGenerator.nextInt(potentialPoints.size()));       // random.nextInt()'s bound is exclusive        

        if (chosenPoint.getX() + 1 >= 0 && chosenPoint.getX() + 1 < grid_size_x) {
            int xPosition = chosenPoint.getX() + 1;
            Point potPathX1 = new Point(xPosition, chosenPoint.getY());
            
            // Check if potPathX1 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potPathX1) == 1 || getValueAt(potPathX1) == 2 || getValueAt(potPathX1) == 3 ) 
            {
                connectedPaths.add(potPathX1);
            }

            addToPotentialPoints(potPathX1);
        }

        if (chosenPoint.getX() - 1 >= 0 && chosenPoint.getX() - 1 < grid_size_x) {
            int xPosition = chosenPoint.getX() + 1;
            Point potPathX2 = new Point(xPosition, chosenPoint.getY());

            // Check if potPathX2 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potPathX2) == 1 || getValueAt(potPathX2) == 2 || getValueAt(potPathX2) == 3 ) 
            {
                connectedPaths.add(potPathX2);
            }
            
            addToPotentialPoints(potPathX2);
        }

        if (chosenPoint.getY() + 1 >= 0 && chosenPoint.getY() + 1 < grid_size_y) {
            int yPosition = chosenPoint.getY() + 1;
            Point potPathY1 = new Point(chosenPoint.getX(), yPosition);

            // Check if potPathY1 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potPathY1) == 1 || getValueAt(potPathY1) == 2 || getValueAt(potPathY1) == 3) 
            {
                connectedPaths.add(potPathY1);
            }
            
            addToPotentialPoints(potPathY1);
        }

        if (chosenPoint.getY() - 1 >= 0 && chosenPoint.getY() - 1 < grid_size_y) {
            int yPosition = chosenPoint.getY() + 1;
            Point potPathY2 = new Point(chosenPoint.getX(), yPosition);

            // Check if potPathY2 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potPathY2) == 1 || getValueAt(potPathY2) == 2 || getValueAt(potPathY2) == 3) 
            {
                connectedPaths.add(potPathY2);
            }
            
            addToPotentialPoints(potPathY2);
        }

        if (connectedPaths.size() >= 2) {
            setMatrix(chosenPoint, 0);          // Set the chosenPoint to a wall because it was connected to 2+ paths
            addToVisual(chosenPoint, 0);
        } else {
            setMatrix(chosenPoint, 1);          // Set the chosenPoint to a path
            addToVisual(chosenPoint, 1);
        }

        return visualPathArray;
    }

    private void addToVisual(Point point, int colorCode) {
        // Add this point as a certain type of block in the visual
        visualPathArray.add(point.getX());
        visualPathArray.add(point.getY());
        visualPathArray.add(colorCode);
    }

    private void setMatrix(Point point, int colorCode) {
        // Add this potential point as a certain type of block in the mazeMatrix
        mazeMatrix.get(point.getX()).set(point.getY(), colorCode);
    }

    private void addToPotentialPoints(Point point) {
        // Add to Potential Points List if the block at this point is a wall.
        if (mazeMatrix.get(point.getX()).get(point.getY()) == 0) {
            potentialPoints.add(point);
            setMatrix(point, 4);
            addToVisual(point, 4);
        }
    }

    private int getValueAt(Point point) {
        return mazeMatrix.get(point.getX()).get(point.getY());
    }
}
