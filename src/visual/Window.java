package visual;
import java.util.ArrayList;

import edu.macalester.graphics.*;
import edu.macalester.graphics.ui.*;
import maze.MazeGenerator;


/* 
 * Responsible for visually representing maze as well as showcasing algorithms and general flow of program 
 */
class Window {
    // All storage values
    private final int CANVAS_WIDTH = 900;
    private final int CANVAS_HEIGHT = 700;
    private final int GRID_SIZE = CANVAS_HEIGHT;
    private int grid_size_x = 20;
    private int grid_size_y = 20;

    // Placeholders
    private ArrayList<ArrayList<Block>> blocks = new ArrayList<>();
    private boolean generating_maze = false; // Tracker to determine if we should be generating a maze or not

    // Other class creations
    private MazeGenerator generator = new MazeGenerator();
    private Sound sound = new Sound();
    private boolean fading = false;

    // All graphical componenets
    private GraphicsText x_label = new GraphicsText();
    private GraphicsText y_label = new GraphicsText();
    private TextField grid_x = new TextField();
    private TextField grid_y = new TextField();
    private Button generate_maze = new Button("Generate Maze");
    private final CanvasWindow canvas = new CanvasWindow("a-maze-ing", CANVAS_WIDTH, CANVAS_HEIGHT);

    public Window() {
        setupUI();
        generateGrid();
        canvas.animate(() -> {
            if (generating_maze) {
                ArrayList<Integer> incoming_updates = generator.update();
                // canvas.pause(400);
                // if the list of updates is empty, then we have finished generating the maze
                if (incoming_updates.isEmpty()) {
                    generating_maze = false;
                    fading = true;
                }
                else {
                    updateVisualGrid(incoming_updates);
                }
            }

            // Check to see if we should fade the sound for an outro
            if (fading) {
                fading = sound.fade();
            }
        });
    }

    /*
     * Setup all UI components
     */
    private void setupUI() {
        // Set all values and text fields
        grid_x.setText(Integer.toString(grid_size_x));
        grid_y.setText(Integer.toString(grid_size_y));
        x_label.setText("Width: ");
        y_label.setText("Height: ");

        // Set locations + scale
        grid_x.setCenter(810, 25);
        grid_y.setCenter(810, 50);
        x_label.setCenter(730, 25);
        y_label.setCenter(730, 50);
        generate_maze.setCenter(800, 75);

        // Setup commands
        grid_x.onChange(string -> {
            grid_size_x = checkInt(string);
            generating_maze = false;
            fading = true;
            generateGrid();
        });
        grid_y.onChange(string -> {
            grid_size_y = checkInt(string);
            generating_maze = false;
            fading = true;
            generateGrid();
        });
        generate_maze.onClick(() -> {
            sound.start(); // Starts lit soundtrack
            generating_maze = true;
            fading = false;
            generateGrid();
        });

        // Add all to canvas
        canvas.add(grid_x);
        canvas.add(grid_y);
        canvas.add(x_label);
        canvas.add(y_label);
        canvas.add(generate_maze);
    }

    /*
     * Creates grid based on input sizes (scales to fit rectangle)
     */
    private void generateGrid() {
        // Remove all the rectangles from the canvas
        blocks.forEach(x -> {
            x.forEach(canvas::remove);
        });
        // Remove all the blocks from the list
        blocks.clear();
        // Tracker to determine the size of the brick so that it's still square and fits in our square showcase
        double block_size;
        if (grid_size_x >= grid_size_y) {
            block_size = 1.0 * GRID_SIZE / grid_size_x;
        }
        else {
            block_size = 1.0 * GRID_SIZE / grid_size_y;
        }
        // Creation of grid
        for (int x = 0; x < grid_size_x; x++) {
            ArrayList<Block> x_column =  new ArrayList<>();
            for (int y = 0; y < grid_size_y; y++) {
                Block block = new Block(0, 0, block_size, block_size);
                block.setPosition(x * block_size, y * block_size); 
                x_column.add(block);
                canvas.add(block);
            }
            blocks.add(x_column);
        }
        // Create grid for mazeGenerator
        updateVisualGrid(generator.setMazeSize(grid_size_x, grid_size_y));
    }

    /*
     * Checks if a string can be converted to an int, otherwise return 2.
     */
    private int checkInt(String potential_int) {
        try {
            int size = Integer.parseInt(potential_int);
            return size < 2 ? 2 : size;
        }
        catch (Exception e) {
            return 2;
        }
    }

    /*
     * Takes in all incoming updates and process them so that they get updated on the grid visually.
     */
    private void updateVisualGrid(ArrayList<Integer> incoming_updates) {
        for (int instruction = 0; instruction < incoming_updates.size(); instruction += 3) {
            int x_coor = incoming_updates.get(instruction);
            int y_coor = incoming_updates.get(instruction + 1);
            int color = incoming_updates.get(instruction + 2);
            blocks.get(x_coor).get(y_coor).setFill(color);
        }
    }


    public static void main(String[] args) {
        new Window();
    }
}

