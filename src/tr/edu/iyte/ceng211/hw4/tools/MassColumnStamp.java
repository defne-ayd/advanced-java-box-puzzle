package tr.edu.iyte.ceng211.hw4.tools;

/**
 * MassColumnStamp is a SpecialTool that stamps an entire column of boxes.
 * 
 * Behavior:
 * - Requires a column number (C1 to C8, or 0-7 internally)
 * - Stamps all 8 boxes in the specified column to the target letter
 * - Has 15% chance of appearing in RegularBox, 20% in UnchangingBox
 * 
 * The generic type T is expected to be the object that provides
 * access to the grid and stamping operations.
 * 
 * @param <T> the target type for tool operations
 */
public class MassColumnStamp<T> extends SpecialTool<T> {
    
    // Number of boxes in a complete column
    private static final int BOXES_PER_COLUMN = 8;
    
    /**
     * Uses the MassColumnStamp tool to stamp an entire column.
     * 
     * The target object should provide:
     * - A method to stamp all boxes in a column
     * - The target letter to stamp
     * - The column number to stamp
     * 
     * @param target the context object containing grid access and parameters
     * @throws IllegalArgumentException if target is null
     */
    @Override
    public void useTool(T target) {
        // Defensive: validate input
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null for MassColumnStamp");
        }
    }
    
    /**
     * Gets the name of this tool.
     * 
     * @return "MassColumnStamp"
     */
    @Override
    public String getToolName() {
        return "MassColumnStamp";
    }
    
    /**
     * Validates that a column number is valid.
     * 
     * @param col the column index (0-7)
     * @return true if valid, false otherwise
     */
    public boolean isValidColumnToStamp(int col) {
        return isValidColumn(col);
    }
    
    /**
     * Gets all positions that would be affected by this tool.
     * Returns an array of {row, col} pairs for all 8 boxes in the column.
     * 
     * @param col the column to stamp (0-7)
     * @return 2D array of positions, each element is {row, col}
     * @throws IllegalArgumentException if column is invalid
     */
    public int[][] getAffectedPositions(int col) {
        // Defensive: validate column
        if (!isValidColumn(col)) {
            throw new IllegalArgumentException(
                String.format("Invalid column: %d. Must be between 0 and 7.", col)
            );
        }
        
        // All 8 rows in the specified column
        int[][] positions = new int[BOXES_PER_COLUMN][2];
        for (int row = 0; row < BOXES_PER_COLUMN; row++) {
            positions[row][0] = row;
            positions[row][1] = col;
        }
        
        return positions;
    }
    
    /**
     * Calculates the number of boxes that will be stamped.
     * For MassColumnStamp, this is always 8.
     * 
     * @return 8 (all boxes in a column)
     */
    public int getAffectedBoxCount() {
        return BOXES_PER_COLUMN;
    }
    
    /**
     * Checks if a specific position is in the affected column.
     * 
     * @param targetRow the row to check (not used, but kept for consistency)
     * @param targetCol the column to check
     * @param stampedCol the column being stamped
     * @return true if targetCol equals stampedCol
     */
    public boolean isAffectedPosition(int targetRow, int targetCol, int stampedCol) {
        // Defensive: validate inputs
        if (!isValidPosition(targetRow, targetCol) || !isValidColumn(stampedCol)) {
            return false;
        }
        
        return targetCol == stampedCol;
    }
    
    /**
     * Converts a user-friendly column number (1-8) to internal index (0-7).
     * 
     * @param userColNumber the column number as displayed to user (C1-C8)
     * @return the internal column index (0-7)
     * @throws IllegalArgumentException if userColNumber is not between 1 and 8
     */
    public int convertUserColToIndex(int userColNumber) {
        if (userColNumber < 1 || userColNumber > GRID_SIZE) {
            throw new IllegalArgumentException(
                String.format("User column number must be between 1 and 8, got: %d", userColNumber)
            );
        }
        return userColNumber - 1;
    }
    
    /**
     * Converts an internal column index (0-7) to user-friendly number (1-8).
     * 
     * @param colIndex the internal column index (0-7)
     * @return the user-friendly column number (1-8)
     * @throws IllegalArgumentException if colIndex is not between 0 and 7
     */
    public int convertIndexToUserCol(int colIndex) {
        if (!isValidColumn(colIndex)) {
            throw new IllegalArgumentException(
                String.format("Column index must be between 0 and 7, got: %d", colIndex)
            );
        }
        return colIndex + 1;
    }
}
