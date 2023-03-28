package maze;

import java.util.ArrayList;

public class Testing {
    ArrayList<ArrayList<Integer>> mazeMatrix;
    ArrayList<Integer> yCoordinates;

    public Testing() {
        mazeMatrix = new ArrayList<ArrayList<Integer>>();
        yCoordinates = new ArrayList<Integer>();
    }

    public static void main(String[] args) {
        Testing test = new Testing();
        int x = 5;
        int y = 5;

        for (int xCoord = 0; xCoord < x; xCoord++){
            test.mazeMatrix.add(new ArrayList<Integer>());
            for (int yCoord = 0; yCoord < y; yCoord++) {
                test.mazeMatrix.get(xCoord).add(0);
            }
        }
        
        System.out.println(test.mazeMatrix);
        test.mazeMatrix.get(0).set(0, 1);
        System.out.println(test.mazeMatrix);
    }
        
}
