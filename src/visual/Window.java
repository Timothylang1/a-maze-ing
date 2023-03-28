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

    // TESTING
    private int tracker = 0;

    public Window() {
        setupUI();
        generateGrid();
        canvas.animate(() -> {
            if (generating_maze) {
                generator.update();
                tracker++;
                if (tracker > 400) {
                    tracker = 0;
                    generating_maze = false;
                    fading = true;
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
            generateGrid();
        });
        grid_y.onChange(string -> {
            grid_size_y = checkInt(string);
            generating_maze = false;
            generateGrid();
        });
        generate_maze.onClick(() -> {
            tracker = 0;
            sound.start(); // Starts lit soundtrack
            generating_maze = true;
            fading = false;
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
        generator.setMazeSize(grid_size_x, grid_size_y);
    }

    /*
     * Checks if a string can be converted to an int, otherwise return 1.
     */
    private int checkInt(String potential_int) {
        try {
            return Integer.parseInt(potential_int);
        }
        catch (Exception e) {
            return 1;
        }
    }


    public static void main(String[] args) {
        new Window();
    }
}

