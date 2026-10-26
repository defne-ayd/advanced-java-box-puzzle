package tr.edu.iyte.ceng211.hw4.tools;

import tr.edu.iyte.ceng211.hw4.exceptions.BoxAlreadyFixedException;

/**
 * BoxFixer is a SpecialTool that converts a box into a FixedBox.
 * 
 * Behavior:
 * - Requires a specific box position
 * - Replaces the box with an identical FixedBox copy
 * - If the box has a tool inside, it is removed from the game
 * - Cannot fix an already FixedBox (throws BoxAlreadyFixedException)
 * - Has 15% chance of appearing in RegularBox, 20% in UnchangingBox
 * 
 * The generic type T is expected to be the object that provides
 * access to the grid and the specific box to fix.
 * 
 * @param <T> the target type for tool operations
 */
public class BoxFixer<T> extends SpecialTool<T> {
    
    // Number of boxes affected by this tool
    private static final int AFFECTED_BOX_COUNT = 1;
    
    /**
     * Uses the BoxFixer tool to convert a box into a FixedBox.
     * 
     * The target object should provide:
     * - A reference to the grid
     * - The position of the box to fix
     * 
     * If the box is already a FixedBox, a BoxAlreadyFixedException should be thrown
     * and the turn will be wasted.
     * 
     * @param target the context object containing grid access and position
     * @throws IllegalArgumentException if target is null
     * @throws BoxAlreadyFixedException if attempting to fix an already FixedBox
     */
    @Override
    public void useTool(T target) {
        // Defensive: validate input
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null for BoxFixer");
        }
    }
    
    /**
     * Gets the name of this tool.
     * 
     * @return "BoxFixer"
     */
    @Override
    public String getToolName() {
        return "BoxFixer";
    }
    
    /**
     * Validates that a box can be fixed.
     * A box cannot be fixed if it is already a FixedBox.
     * 
     * This helper method can be called before attempting to fix.
     * 
     * @param isFixed whether the box is already fixed
     * @return true if the box can be fixed, false otherwise
     */
    public boolean canFixBox(boolean isFixed) {
        return !isFixed;
    }
    
    /**
     * Gets the single position that would be affected by this tool.
     * 
     * @param row the row of the box to fix (0-7)
     * @param col the column of the box to fix (0-7)
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
     * For BoxFixer, this is always 1.
     * 
     * @return 1 (only the fixed box)
     */
    public int getAffectedBoxCount() {
        return AFFECTED_BOX_COUNT;
    }
    
    /**
     * Checks if fixing a box would result in content loss.
     * When a box is converted to FixedBox, any tool inside is removed.
     * 
     * @param boxHasContent whether the box has a tool inside
     * @return true if content will be lost, false otherwise
     */
    public boolean willLoseContent(boolean boxHasContent) {
        return boxHasContent;
    }
    
    /**
     * Checks if a specific position will be affected by the fix operation.
     * 
     * @param targetRow the row to check
     * @param targetCol the column to check
     * @param fixedRow the row of the box being fixed
     * @param fixedCol the column of the box being fixed
     * @return true if (targetRow, targetCol) equals (fixedRow, fixedCol)
     */
    public boolean isAffectedPosition(int targetRow, int targetCol, int fixedRow, int fixedCol) {
        // Defensive: validate inputs
        if (!isValidPosition(targetRow, targetCol) || !isValidPosition(fixedRow, fixedCol)) {
            return false;
        }
        
        return targetRow == fixedRow && targetCol == fixedCol;
    }
    
    /**
     * Validates that the fix operation is allowed on the given box type.
     * 
     * @param isFixed whether the target box is already a FixedBox
     * @return true if the fix is allowed (box is not already fixed)
     */
    public boolean isFixAllowed(boolean isFixed) {
        return !isFixed;
    }
    
    /**
     * Provides information about what will happen when fixing a box.
     * This can be used to warn the player about content loss.
     * 
     * @param hasContent whether the box has content
     * @return a descriptive message about the operation result
     */
    public String getOperationDescription(boolean hasContent) {
        if (hasContent) {
            return "This box contains a tool. Fixing it will remove the tool permanently.";
        } else {
            return "This box is empty. It will be converted to a FixedBox.";
        }
    }
}
