package tr.edu.iyte.ceng211.hw4.tools;

import tr.edu.iyte.ceng211.hw4.exceptions.UnmovableFixedBoxException;

/**
 * BoxFlipper is a SpecialTool that flips a box upside down.
 * 
 * Behavior:
 * - Requires a specific box position
 * - Flips the box so top becomes bottom and vice versa
 * - Cannot flip a FixedBox (throws UnmovableFixedBoxException)
 * - Has 15% chance of appearing in RegularBox, 20% in UnchangingBox
 * 
 * The generic type T is expected to be the object that provides
 * access to the specific box to flip.
 * 
 * @param <T> the target type for tool operations
 */
public class BoxFlipper<T> extends SpecialTool<T> {
    
    // Number of boxes affected by this tool
    private static final int AFFECTED_BOX_COUNT = 1;
    
    /**
     * Uses the BoxFlipper tool to flip a box upside down.
     * 
     * The target object should provide:
     * - A reference to the specific box to flip
     * - The position of the box
     * 
     * If the box is a FixedBox, an UnmovableFixedBoxException should be thrown
     * by the box's flipUpsideDown() method, and the turn will be wasted.
     * 
     * @param target the context object containing the box reference
     * @throws IllegalArgumentException if target is null
     * @throws UnmovableFixedBoxException if attempting to flip a FixedBox (thrown by Box)
     */
    @Override
    public void useTool(T target) {
        // Defensive: validate input
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null for BoxFlipper");
        }
    }
    
    /**
     * Gets the name of this tool.
     * 
     * @return "BoxFlipper"
     */
    @Override
    public String getToolName() {
        return "BoxFlipper";
    }
    
    /**
     * Validates that a box can be flipped.
     * A box cannot be flipped if it is a FixedBox.
     * 
     * This helper method can be called before attempting to flip.
     * 
     * @param isFixed whether the box is fixed
     * @return true if the box can be flipped, false otherwise
     */
    public boolean canFlipBox(boolean isFixed) {
        return !isFixed;
    }
    
    /**
     * Gets the single position that would be affected by this tool.
     * 
     * @param row the row of the box to flip (0-7)
     * @param col the column of the box to flip (0-7)
     * @return 2D array with one position {row, col}
     * @throws IllegalArgumentException if position is invalid
     */
    public int[][] getAffectedPositions(int row, int col) {
        // Defensive: validate position
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                String.format("Invalid position: (%d, %d). Must be between 0-7.", row, col)
            );
        }
        
        return new int[][]{{row, col}};
    }
    
    /**
     * Calculates the number of boxes that will be affected.
     * For BoxFlipper, this is always 1.
     * 
     * @return 1 (only the flipped box)
     */
    public int getAffectedBoxCount() {
        return AFFECTED_BOX_COUNT;
    }
    
    /**
     * Checks if a specific position will be affected by the flip operation.
     * 
     * @param targetRow the row to check
     * @param targetCol the column to check
     * @param flippedRow the row of the box being flipped
     * @param flippedCol the column of the box being flipped
     * @return true if (targetRow, targetCol) equals (flippedRow, flippedCol)
     */
    public boolean isAffectedPosition(int targetRow, int targetCol, int flippedRow, int flippedCol) {
        // Defensive: validate inputs
        if (!isValidPosition(targetRow, targetCol) || !isValidPosition(flippedRow, flippedCol)) {
            return false;
        }
        
        return targetRow == flippedRow && targetCol == flippedCol;
    }
    
    /**
     * Validates that the flip operation is allowed on the given box type.
     * 
     * @param isFixed whether the target box is a FixedBox
     * @param isUnchanging whether the target box is an UnchangingBox
     * @return true if the flip is allowed
     */
    public boolean isFlipAllowed(boolean isFixed, boolean isUnchanging) {
        // FixedBox cannot be flipped
        // UnchangingBox CAN be flipped (only stamping is restricted)
        return !isFixed;
    }
}
