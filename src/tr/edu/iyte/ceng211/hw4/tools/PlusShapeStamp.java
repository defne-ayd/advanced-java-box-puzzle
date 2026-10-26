package tr.edu.iyte.ceng211.hw4.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * PlusShapeStamp is a SpecialTool that stamps 5 boxes in a plus (+) shape.
 * 
 * Behavior:
 * - Requires a center box position
 * - Stamps the center box and its 4 neighbors (up, down, left, right)
 * - All affected boxes are stamped with the target letter
 * - Boxes on the edge may have fewer than 4 neighbors
 * - Has 15% chance of appearing in RegularBox, 20% in UnchangingBox
 * 
 * The generic type T is expected to be the object that provides
 * access to the grid and stamping operations (likely BoxGrid or a context object).
 * 
 * @param <T> the target type for tool operations
 */
public class PlusShapeStamp<T> extends SpecialTool<T> {
    
    // Number of maximum affected boxes (center + 4 neighbors)
    private static final int MAX_AFFECTED_BOXES = 5;
    
    /**
     * Uses the PlusShapeStamp tool to stamp 5 boxes.
     * 
     * The target object should provide:
     * - A method to stamp a box at a given position
     * - The target letter to stamp
     * - The center position to stamp around
     * 
     * @param target the context object containing grid access and parameters
     * @throws IllegalArgumentException if target is null
     */
    @Override
    public void useTool(T target) {
        // Defensive: validate input
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null for PlusShapeStamp");
        }
    }
    
    /**
     * Gets the name of this tool.
     * 
     * @return "PlusShapeStamp"
     */
    @Override
    public String getToolName() {
        return "PlusShapeStamp";
    }
    
    /**
     * Validates that a plus shape centered at (row, col) is valid.
     * This is a helper method for the Game Controller to use.
     * 
     * Note: A plus shape is still valid even if some neighbors are out of bounds.
     * Only the center must be valid.
     * 
     * @param row the center row
     * @param col the center column
     * @return true if the center position is valid
     */
    public boolean isValidPlusShape(int row, int col) {
        return isValidPosition(row, col);
    }
    
    /**
     * Gets the positions that would be affected by this tool.
     * Returns a list of {row, col} pairs for the center and up to 4 neighbors.
     * 
     * @param centerRow the center row
     * @param centerCol the center column
     * @return 2D array of positions, each row is {row, col}
     * @throws IllegalArgumentException if center position is invalid
     */
    public int[][] getAffectedPositions(int centerRow, int centerCol) {
        // Defensive: validate center position
        if (!isValidPosition(centerRow, centerCol)) {
            throw new IllegalArgumentException(
                String.format("Invalid center position: (%d, %d). Must be between 0-7.", 
                    centerRow, centerCol)
            );
        }
        
        // Use a list to collect valid positions dynamically
        List<int[]> positionsList = new ArrayList<>();
        
        // Center position (always included)
        positionsList.add(new int[]{centerRow, centerCol});
        
        // Up neighbor
        if (isValidRow(centerRow - 1)) {
            positionsList.add(new int[]{centerRow - 1, centerCol});
        }
        
        // Down neighbor
        if (isValidRow(centerRow + 1)) {
            positionsList.add(new int[]{centerRow + 1, centerCol});
        }
        
        // Left neighbor
        if (isValidColumn(centerCol - 1)) {
            positionsList.add(new int[]{centerRow, centerCol - 1});
        }
        
        // Right neighbor
        if (isValidColumn(centerCol + 1)) {
            positionsList.add(new int[]{centerRow, centerCol + 1});
        }
        
        // Convert list to array
        int[][] positions = new int[positionsList.size()][2];
        for (int i = 0; i < positionsList.size(); i++) {
            positions[i] = positionsList.get(i);
        }
        
        return positions;
    }
    
    /**
     * Calculates the number of boxes that will be affected.
     * This depends on the center position (corner, edge, or middle).
     * 
     * @param centerRow the center row
     * @param centerCol the center column
     * @return the number of boxes that will be stamped (2-5)
     * @throws IllegalArgumentException if center position is invalid
     */
    public int getAffectedBoxCount(int centerRow, int centerCol) {
        return getAffectedPositions(centerRow, centerCol).length;
    }
    
    /**
     * Checks if a specific position is one of the neighbors of the center.
     * 
     * @param row the row to check
     * @param col the column to check
     * @param centerRow the center row
     * @param centerCol the center column
     * @return true if (row, col) is a neighbor of (centerRow, centerCol)
     */
    public boolean isNeighbor(int row, int col, int centerRow, int centerCol) {
        // Defensive: validate inputs
        if (!isValidPosition(row, col) || !isValidPosition(centerRow, centerCol)) {
            return false;
        }
        
        // Check if it's the center itself
        if (row == centerRow && col == centerCol) {
            return true;
        }
        
        // Check if it's directly up, down, left, or right
        boolean isUpNeighbor = (row == centerRow - 1 && col == centerCol);
        boolean isDownNeighbor = (row == centerRow + 1 && col == centerCol);
        boolean isLeftNeighbor = (row == centerRow && col == centerCol - 1);
        boolean isRightNeighbor = (row == centerRow && col == centerCol + 1);
        
        return isUpNeighbor || isDownNeighbor || isLeftNeighbor || isRightNeighbor;
    }
}
