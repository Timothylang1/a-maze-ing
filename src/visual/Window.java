package visual;
import java.util.ArrayList;
import java.awt.Color;

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
    private final int GRID_SIDELENGTH_CAP = 200; // Maximum gridsize we allow, because anything bigger lags the animation too much
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
    private GraphicsText actual_grid_size = new GraphicsText();
    private Button generate_maze = new Button("Generate Maze");
    private final CanvasWindow canvas = new CanvasWindow("a-maze-ing", CANVAS_WIDTH, CANVAS_HEIGHT);

    // Pacing variables
    private final int FRAMES_PER_SECOND = 60;
    private final int TOTAL_SECONDS_TO_SOLVE_MAZE = 5;
    private final double UPDATE_CALLS_TO_GRID_SIZE_RATIO = 1.2; // Calculated ratio (from testing file in visual) that determines how many times update is needed to be called based on a gridsize
    private double updates_per_frame;
    private int current_update_iteration;
    private double current_update_checkpoint;

    public Window() {
        setupUI();
        generateGrid();
        canvas.animate(() -> {
            if (generating_maze) {
                // While loop will call updates as many times as nessecary to ensure that we are on track to finishing completing the maze in TOTAL_SECONDS_TO_SOLVE_MAZE amount of time
                while (updates_per_frame * current_update_checkpoint > current_update_iteration) {
                    // Call update
                    ArrayList<Integer> incoming_updates = generator.update();
                    // If the list of updates is empty, then we have finished generating the maze
                    if (incoming_updates.isEmpty()) {
                        generating_maze = false;
                        fading = true;
                    }
                    else {
                        // Update grid to match visually
                        updateVisualGrid(incoming_updates);
                    }
                    // Update tracker for pacing
                    current_update_iteration++;
                }
                current_update_checkpoint += 1;
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
        x_label.setText("Width: ");
        y_label.setText("Height: ");
        grid_x.setText(Integer.toString(grid_size_x));
        grid_y.setText(Integer.toString(grid_size_y));
        actual_grid_size.setText("Grid size: " + Integer.toString(grid_size_x) + "x" + Integer.toString(grid_size_y));
        actual_grid_size.setFontSize(15);
        actual_grid_size.setFillColor(new Color(255, 20, 20));

        // Set locations
        x_label.setCenter(730, 25);
        y_label.setCenter(730, 50);
        grid_x.setCenter(810, 25);
        grid_y.setCenter(810, 50);
        actual_grid_size.setCenter(785, 75);
        generate_maze.setCenter(800, 100);

        // Setup commands
        grid_x.onChange(string -> {
            grid_size_x = checkInt(string);
            createGrid();
        });
        grid_y.onChange(string -> {
            grid_size_y = checkInt(string);
            createGrid();
        });
        generate_maze.onClick(() -> {
            startMaze();
        });

        // Add all to canvas
        canvas.add(grid_x);
        canvas.add(grid_y);
        canvas.add(actual_grid_size);
        canvas.add(x_label);
        canvas.add(y_label);
        canvas.add(generate_maze);

        // Setup canvas
        canvas.setBackground(new Color(200, 200, 200));
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
     * Resets color of all rectangles instead of deleting all rectangles (more efficient method than generating an entirely
     * new grid every time we click generateGrid). Also starts soundtrack and sets counters to continue updating grid.
     */
    private void startMaze() {
        sound.start(); // Starts lit soundtrack
        generating_maze = true;
        fading = false;
        updates_per_second();
        blocks.forEach(column -> {
            column.forEach(block -> {
                block.setFill(0); // Reset all blocks back to walls
            });
        });
        updateVisualGrid(generator.setMazeSize(grid_size_x, grid_size_y)); // Add in start and ending block
    }

    /*
     * Creates grid and updates counters accordingly
     */
    private void createGrid() {
        actual_grid_size.setText("Grid size: " + Integer.toString(grid_size_x) + "x" + Integer.toString(grid_size_y));
        generating_maze = false;
        fading = true;
        generateGrid();
    }

    /*
     * Checks to see if its possible to convert to an int, and if no returns 2.
     * Before returning original value, check to see if it's within bounds (0 and GRIDCAP / 2)
     */
    private int checkInt(String potential_int) {
        try {
            int size = Integer.parseInt(potential_int);
            if (size < 2) return 2;
            if (size > GRID_SIDELENGTH_CAP) return GRID_SIDELENGTH_CAP;
            return size;
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

    /*
     * Sets a pace for calling updates per frame so that it doesn't take forever to solve the maze
     */
    private void updates_per_second() {
        // Calculate the total number of updates needed based on gridsize
        double total_updates = UPDATE_CALLS_TO_GRID_SIZE_RATIO * grid_size_x * grid_size_y;
        
        // Number of updates per frame
        updates_per_frame = total_updates / (FRAMES_PER_SECOND * TOTAL_SECONDS_TO_SOLVE_MAZE);

        // Reset trackers back to 0
        current_update_iteration = 0;
        current_update_checkpoint = 0;
    }


    public static void main(String[] args) {
        new Window();
    }
}

