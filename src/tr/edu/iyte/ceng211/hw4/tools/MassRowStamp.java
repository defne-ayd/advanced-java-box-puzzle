package tr.edu.iyte.ceng211.hw4.tools;

/**
 * MassRowStamp is a SpecialTool that stamps an entire row of boxes.
 * 
 * Behavior:
 * - Requires a row number (R1 to R8, or 0-7 internally)
 * - Stamps all 8 boxes in the specified row to the target letter
 * - Has 15% chance of appearing in RegularBox, 20% in UnchangingBox
 * 
 * The generic type T is expected to be the object that provides
 * access to the grid and stamping operations.
 * 
 * @param <T> the target type for tool operations
 */
public class MassRowStamp<T> extends SpecialTool<T> {
    
    // Number of boxes in a complete row
    private static final int BOXES_PER_ROW = 8;
    
    /**
     * Uses the MassRowStamp tool to stamp an entire row.
     * 
     * The target object should provide:
     * - A method to stamp all boxes in a row
     * - The target letter to stamp
     * - The row number to stamp
     * 
     * @param target the context object containing grid access and parameters
     * @throws IllegalArgumentException if target is null
     */
    @Override
    public void useTool(T target) {
        // Defensive: validate input
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null for MassRowStamp");
        }
    }
    
    /**
     * Gets the name of this tool.
     * 
     * @return "MassRowStamp"
     */
    @Override
    public String getToolName() {
        return "MassRowStamp";
    }
    
    /**
     * Validates that a row number is valid.
     * 
     * @param row the row index (0-7)
     * @return true if valid, false otherwise
     */
    public boolean isValidRowToStamp(int row) {
        return isValidRow(row);
    }
    
    /**
     * Gets all positions that would be affected by this tool.
     * Returns an array of {row, col} pairs for all 8 boxes in the row.
     * 
     * @param row the row to stamp (0-7)
     * @return 2D array of positions, each element is {row, col}
     * @throws IllegalArgumentException if row is invalid
     */
    public int[][] getAffectedPositions(int row) {
        // Defensive: validate row
        if (!isValidRow(row)) {
            throw new IllegalArgumentException(
                String.format("Invalid row: %d. Must be between 0 and 7.", row)
            );
        }
        
        // All 8 columns in the specified row
        int[][] positions = new int[BOXES_PER_ROW][2];
        for (int col = 0; col < BOXES_PER_ROW; col++) {
            positions[col][0] = row;
            positions[col][1] = col;
        }
        
        return positions;
    }
    
    /**
     * Calculates the number of boxes that will be stamped.
     * For MassRowStamp, this is always 8.
     * 
     * @return 8 (all boxes in a row)
     */
    public int getAffectedBoxCount() {
        return BOXES_PER_ROW;
    }
    
    /**
     * Checks if a specific position is in the affected row.
     * 
     * @param targetRow the row to check
     * @param targetCol the column to check (not used, but kept for consistency)
     * @param stampedRow the row being stamped
     * @return true if targetRow equals stampedRow
     */
    public boolean isAffectedPosition(int targetRow, int targetCol, int stampedRow) {
        // Defensive: validate inputs
        if (!isValidPosition(targetRow, targetCol) || !isValidRow(stampedRow)) {
            return false;
        }
        
        return targetRow == stampedRow;
    }
    
    /**
     * Converts a user-friendly row number (1-8) to internal index (0-7).
     * 
     * @param userRowNumber the row number as displayed to user (R1-R8)
     * @return the internal row index (0-7)
     * @throws IllegalArgumentException if userRowNumber is not between 1 and 8
     */
    public int convertUserRowToIndex(int userRowNumber) {
        if (userRowNumber < 1 || userRowNumber > GRID_SIZE) {
            throw new IllegalArgumentException(
                String.format("User row number must be between 1 and 8, got: %d", userRowNumber)
            );
        }
        return userRowNumber - 1;
    }
    
    /**
     * Converts an internal row index (0-7) to user-friendly number (1-8).
     * 
     * @param rowIndex the internal row index (0-7)
     * @return the user-friendly row number (1-8)
     * @throws IllegalArgumentException if rowIndex is not between 0 and 7
     */
    public int convertIndexToUserRow(int rowIndex) {
        if (!isValidRow(rowIndex)) {
            throw new IllegalArgumentException(
                String.format("Row index must be between 0 and 7, got: %d", rowIndex)
            );
        }
        return rowIndex + 1;
    }
}
