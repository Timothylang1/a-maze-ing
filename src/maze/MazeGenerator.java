package maze;

import java.util.ArrayList;
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
    }

    public ArrayList<Integer> setMazeSize(int x, int y) {
        // Create a matrix of size x and y. All values set to zero at first because everything is a wall.
        // The matrix will have the coordinate system (x, y)
        // Returns the start and end block
        mazeMatrix.clear();
        visualPathArray.clear();
        potentialPoints.clear();
        
        grid_size_x = x;
        grid_size_y = y;
        for (int xCoord = 0; xCoord < x; xCoord++){
            mazeMatrix.add(new ArrayList<Integer>());
            for (int yCoord = 0; yCoord < y; yCoord++) {
                mazeMatrix.get(xCoord).add(0);
            }
        }

        // Update Starting Block
        setMatrix(new Point(0, 0), 2);
        addToVisual(new Point(0, 0), 2);
        addToPotentialPoints(new Point(1, 0));
        addToPotentialPoints(new Point(0, 1));



        // Update Ending Block 
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
        
        visualPathArray.clear();
        // System.out.println(mazeMatrix);

        // Potential paths are 1 block away from a current block.
        // From the start, I can't go negative direction.
    
        // Remove the chosenPoint from the potentialPaths list
        if (potentialPoints.size() == 0) {
            Point endPoint = new Point(grid_size_x - 1, grid_size_y - 1);
            Point endPointLeft = new Point(endPoint.getX() - 1, endPoint.getY());
            Point endPointUp = new Point(endPoint.getX(), endPoint.getY() - 1);

            if (getValueAt(endPointLeft) == 0 && getValueAt(endPointUp) == 0) {
                ArrayList<Point> endings = new ArrayList<Point>();
                endings.add(endPointLeft);
                endings.add(endPointUp);
    
                Point chosenEnding = endings.get(randomGenerator.nextInt(endings.size()));
                setMatrix(chosenEnding, 1);
                addToVisual(chosenEnding, 1);
            }

            return visualPathArray;
        }
        Point chosenPoint = potentialPoints.remove(randomGenerator.nextInt(potentialPoints.size()));       // random.nextInt()'s bound is exclusive        

        Point potRightPoint = new Point(chosenPoint.getX() + 1, chosenPoint.getY());
        Point potLeftPoint = new Point(chosenPoint.getX() - 1, chosenPoint.getY());
        Point potBottomPoint = new Point(chosenPoint.getX(), chosenPoint.getY() + 1);
        Point potTopPoint = new Point(chosenPoint.getX(), chosenPoint.getY() - 1);
        ArrayList<Point> neighborPoints = new ArrayList<Point>();




        // Right Point
        if (potRightPoint.getX() < grid_size_x) {            
            // Check if potPathX1 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potRightPoint) == 1 || getValueAt(potRightPoint) == 2 || getValueAt(potRightPoint) == 3 ) 
            {
                neighborPoints.add(potRightPoint);
            }
        }

        // Left Point
        if (potLeftPoint.getX() >= 0) {

            // Check if potPathX2 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potLeftPoint) == 1 || getValueAt(potLeftPoint) == 2 || getValueAt(potLeftPoint) == 3 ) 
            {
                neighborPoints.add(potLeftPoint);
            }
        }

        // Bottom Point
        if (potBottomPoint.getY() < grid_size_y) {

            // Check if potPathY1 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potBottomPoint) == 1 || getValueAt(potBottomPoint) == 2 || getValueAt(potBottomPoint) == 3) 
            {
                neighborPoints.add(potBottomPoint);
            }
        }

        // Top Point
        if (potTopPoint.getY() >= 0) {

            // Check if potPathY2 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
            if (getValueAt(potTopPoint) == 1 || getValueAt(potTopPoint) == 2 || getValueAt(potTopPoint) == 3) 
            {
                neighborPoints.add(potTopPoint);
            }            
        }

        if (neighborPoints.size() == 2) {
            setMatrix(chosenPoint, 0);          // Set the chosenPoint to a wall because it was connected to 2+ paths
            addToVisual(chosenPoint, 0);
        } else {
            setMatrix(chosenPoint, 1);          // Set the chosenPoint to a path
            addToVisual(chosenPoint, 1);

            // Calculating where the next path will be placed based on previous path's direction.
            int nextPointX = 2*(chosenPoint.getX()) - neighborPoints.get(0).getX();
            int nextPointY = 2*(chosenPoint.getY()) - neighborPoints.get(0).getY();
            Point nextPoint = new Point(nextPointX, nextPointY);

            System.out.println("This is neighborPoints.get(0).getX() " + neighborPoints.get(0).getX());
            System.out.println("This is neighborPoints.get(0).getY() " + neighborPoints.get(0).getY());

            System.out.println("This is nextPointX " + nextPointX);
            System.out.println("This is nextPointY " + nextPointY);
            System.out.println("This is chosenPointX " + chosenPoint.getX());
            System.out.println("This is chosenPointY " + chosenPoint.getY());


            setMatrix(nextPoint, 1);
            addToVisual(nextPoint, 1);

            potRightPoint = new Point(nextPoint.getX() + 1, nextPoint.getY());
            potLeftPoint = new Point(nextPoint.getX() - 1, nextPoint.getY());
            potBottomPoint = new Point(nextPoint.getX(), nextPoint.getY() + 1);
            potTopPoint = new Point(nextPoint.getX(), nextPoint.getY() - 1);


            // Right Point
            if (potRightPoint.getX() < grid_size_x) {            
                // Check if potPathX1 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
                addToPotentialPoints(potRightPoint);
            }

            // Left Point
            if (potLeftPoint.getX() >= 0) {

                // Check if potPathX2 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
                addToPotentialPoints(potLeftPoint);
            }

            // Bottom Point
            if (potBottomPoint.getY() < grid_size_y) {

                // Check if potPathY1 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
                addToPotentialPoints(potBottomPoint);
            }

            // Top Point
            if (potTopPoint.getY() >= 0) {

                // Check if potPathY2 is labeled as a path or not. If yes, add it to connectedPaths; If not, don't add to connectedPaths
                addToPotentialPoints(potTopPoint);
            }
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
        if (getValueAt(point) == 0) {
            potentialPoints.add(point);
            setMatrix(point, 4);
            addToVisual(point, 4);
        }
    }

    private int getValueAt(Point point) {
        return mazeMatrix.get(point.getX()).get(point.getY());
    }
}
