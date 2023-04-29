package maze;

import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator {
    public static ArrayList<ArrayList<Integer>> mazeMatrix;
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
        addToNeighborPoints(potRightPoint, neighborPoints);
        
        // Left Point
        addToNeighborPoints(potLeftPoint, neighborPoints);

        // Bottom Point
        addToNeighborPoints(potBottomPoint, neighborPoints);

        // Top Point
        addToNeighborPoints(potTopPoint, neighborPoints);

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


            setMatrix(nextPoint, 1);
            addToVisual(nextPoint, 1);

            potRightPoint = new Point(nextPoint.getX() + 1, nextPoint.getY());
            potLeftPoint = new Point(nextPoint.getX() - 1, nextPoint.getY());
            potBottomPoint = new Point(nextPoint.getX(), nextPoint.getY() + 1);
            potTopPoint = new Point(nextPoint.getX(), nextPoint.getY() - 1);


            // Right Point
            addToPotentialPoints(potRightPoint);
            
            // Left Point
            addToPotentialPoints(potLeftPoint);

            // Bottom Point
            addToPotentialPoints(potBottomPoint);

            // Top Point
            addToPotentialPoints(potTopPoint);
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
        // Add to Potential Points List if the block is in bounds AND if at this point is a wall.
        if (checkBounds(point)) {
            if (getValueAt(point) == 0) {
                potentialPoints.add(point);
                setMatrix(point, 4);
                addToVisual(point, 4);
            }    
        }
    }

    private int getValueAt(Point point) {
        return mazeMatrix.get(point.getX()).get(point.getY());
    }

    private void addToNeighborPoints(Point point, ArrayList<Point> neighborPoints) {
        if (checkBounds(point)){
            if (getValueAt(point) == 1 || getValueAt(point) == 2 || getValueAt(point) == 3 ) 
            {
                neighborPoints.add(point);
            }    
        }
    }

    private boolean checkBounds(Point point) {
        if ((point.getX() < grid_size_x) && (point.getX() >= 0)) {
            if ((point.getY() < grid_size_y) && (point.getY() >= 0)) {
                return true;
            }
        }
        return false;
    }
}
