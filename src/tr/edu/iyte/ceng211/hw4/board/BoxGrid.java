package tr.edu.iyte.ceng211.hw4.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tr.edu.iyte.ceng211.hw4.boxes.*;
import tr.edu.iyte.ceng211.hw4.exceptions.UnmovableFixedBoxException;

public class BoxGrid {

    /*
     * ANSWER TO COLLECTIONS QUESTION:
     * I chose to use an ArrayList of ArrayLists (List<List<Box>>) to represent the grid. 
     * This decision was made because the game relies heavily on 2D coordinates (rows and columns).
     * A nested List structure allows for direct, readable access via grid.get(row).get(col), 
     * matching the game's logic perfectly. It also makes row-specific operations efficient 
     * and intuitive compared to a single flattened 1D list.
     */
    private List<List<Box>> grid;
    private static final int GRID_SIZE = 8;

    public BoxGrid() {
        this.grid = new ArrayList<>();
        generateGrid();
    }
    
    /**
     * Populates the grid with 8x8 boxes based on the probability rules:
     * 85% Regular, 10% Unchanging, 5% Fixed.
     */
    private void generateGrid() {
        // Clear existing grid if any
        this.grid.clear();
        
        for (int row = 0; row < GRID_SIZE; row++) {
            List<Box> rowList = new ArrayList<>();
            for (int col = 0; col < GRID_SIZE; col++) {
                rowList.add(generateRandomBox());
            }
            this.grid.add(rowList);
        }
    }

    /**
     * Prints the current state of the grid to the console.
     */
public void printGrid() {
    System.out.println("\nCurrent Grid State:");

    // cell width based on Box.toString() so headers match whatever your Box prints
    int cellWidth = this.grid.get(0).get(0).toString().length();

    // Row label part: "R1 " is 3 chars, plus one extra space before cells
    String rowPrefix = "R1 ";
    int leftPad = rowPrefix.length() + 1;

    // ---- Column headers (C1..C8) ----
    System.out.print(" ".repeat(leftPad));
    for (int col = 1; col <= GRID_SIZE; col++) {
        String h = "C" + col;
        System.out.print(center(h, cellWidth));
    }
    System.out.println();

    // ---- Top dashed line ----
    System.out.print(" ".repeat(leftPad));
    System.out.println("-".repeat(cellWidth * GRID_SIZE));

    // ---- Rows ----
    for (int row = 0; row < GRID_SIZE; row++) {
        // Row label like PDF: R1, R2...
        System.out.print("R" + (row + 1) + " |");

        for (int col = 0; col < GRID_SIZE; col++) {
            System.out.print(this.grid.get(row).get(col).toString());
        }
        System.out.println();

        // dashed line after every row (matches the PDF look)
        System.out.print(" ".repeat(leftPad));
        System.out.println("-".repeat(cellWidth * GRID_SIZE));
    }

    System.out.println();
}

/**
 * Centers text inside a fixed-width field.
 */
private String center(String text, int width) {
    if (text.length() >= width) return text.substring(0, width);
    int left = (width - text.length()) / 2;
    int right = width - text.length() - left;
    return " ".repeat(left) + text + " ".repeat(right);
}



    /**
     * Gets a specific box from the grid.
     * Useful for Menu interactions.
     */
    public Box getBox(int row, int col) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            return null;
        }
        return this.grid.get(row).get(col);
    }
    
    /**
     * Sets a specific box in the grid.
     * Useful for SpecialTools (e.g. BoxFixer).
     */
    public void setBox(int row, int col, Box newBox) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            this.grid.get(row).set(col, newBox);
        }
    }

    /**
     * Executes the "Domino Effect" roll logic.
     * 1. Checks if start is Fixed.
     * 2. Scans for the "Limit" (Wall/Edge).
     * 3. Shifts boxes towards the limit (crushing the one before the limit).
     * 4. Spawns new box at start.
     */
    public void roll(int startRow, int startCol, Direction dir) {
        
        // Validate coordinates first
        if (startRow < 0 || startRow >= GRID_SIZE || startCol < 0 || startCol >= GRID_SIZE) {
            System.out.println("Error: coordinates out of bounds.");
            return;
        }

        // 1. Check if the starting box itself is Fixed
        if (this.grid.get(startRow).get(startCol).isFixed()) {
            throw new UnmovableFixedBoxException("Cannot roll a FixedBox at R" + (startRow+1) + "-C" + (startCol+1));
        }

        // 2. Scan Phase: Find the "Limit" (The Obstacle)
        // An obstacle is either the Edge of the grid OR a FixedBox.
        int scanRow = startRow;
        int scanCol = startCol;
        
        int limitRow = startRow;
        int limitCol = startCol;

        while (true) { 
            int nextRow = scanRow + dir.getDeltaRow();
            int nextCol = scanCol + dir.getDeltaCol();

            // Check if we hit the Edge
            if (nextRow < 0 || nextRow >= GRID_SIZE || nextCol < 0 || nextCol >= GRID_SIZE) {
                // The limit is outside the board (effectively the edge index)
                limitRow = nextRow;
                limitCol = nextCol;
                break; 
            }

            // Check if we hit a FixedBox (The Wall)
            if (this.grid.get(nextRow).get(nextCol).isFixed()) {
                // The limit is the FixedBox itself
                limitRow = nextRow;
                limitCol = nextCol;
                break; 
            }

            // Keep scanning
            scanRow = nextRow;
            scanCol = nextCol;
        }

        // 3. Shift Phase: Work Backwards
        // We start shifting INTO the cell *before* the limit.
        // We must NEVER overwrite the Limit if it is a FixedBox.
        // So destination starts at limit - delta.
        
        int destRow = limitRow - dir.getDeltaRow();
        int destCol = limitCol - dir.getDeltaCol();

        // Loop backwards from the limit to the start.
        // If dest == start (meaning the wall is right next to us), this loop is skipped.
        
        while (destRow != startRow || destCol != startCol) {
            
            // The box we are moving is the one behind the destination
            int srcRow = destRow - dir.getDeltaRow();
            int srcCol = destCol - dir.getDeltaCol();

            // Get the box to move
            Box boxToMove = this.grid.get(srcRow).get(srcCol);
            
            // Rotate the box as it moves
            boxToMove.roll(dir);

            // Move it to the destination (overwriting whatever was there - "Crushing")
            this.grid.get(destRow).set(destCol, boxToMove);

            // Step backwards
            destRow = srcRow;
            destCol = srcCol;
        }

        // 4. Fill the Void
        // The start position is now technically "empty" (contains a duplicate).
        // Generate a new random box to fill the edge.
        Box newBox = generateRandomBox();
        this.grid.get(startRow).set(startCol, newBox);
    }

    // Helper
    private Box generateRandomBox() {
        int chance = new Random().nextInt(100);
        if (chance < 85) {
            return new RegularBox();
        } else if (chance < 95) { // 85 to 94 is 10%
            return new UnchangingBox();
        } else { // 95 to 99 is 5%
            return new FixedBox();
        }
    }
}