package tr.edu.iyte.ceng211.hw4.tools;

/**
 * Abstract base class for all SpecialTools in the puzzle game.
 * Uses generics to allow type-safe tool usage with different box types.
 * 
 * The generic type T represents the target type for the tool operation.
 * This enables polymorphic tool usage through the useTool() method.
 * 
 * All tools must implement:
 * - useTool(): The main action of the tool
 * - getToolName(): A descriptive name for display
 * 
 * Tool Types:
 * 1. PlusShapeStamp - Stamps 5 boxes in a plus shape
 * 2. MassRowStamp - Stamps an entire row
 * 3. MassColumnStamp - Stamps an entire column
 * 4. BoxFlipper - Flips a box upside down
 * 5. BoxFixer - Converts a box to a FixedBox
 * 
 * @param <T> the type parameter for the tool (typically related to grid operations)
 */
public abstract class SpecialTool<T> {
    
    // Grid size constants for validation
    protected static final int GRID_SIZE = 8;
    protected static final int MIN_INDEX = 0;
    protected static final int MAX_INDEX = GRID_SIZE - 1;
    
    // Valid letter range
    protected static final char MIN_LETTER = 'A';
    protected static final char MAX_LETTER = 'H';
    
    /**
     * The main method that executes the tool's special ability.
     * This method is called when the tool is acquired and must be used immediately.
     * 
     * The parameter T allows for flexible implementation - it could be:
     * - A BoxGrid reference for grid manipulation
     * - A specific box reference for single-box operations
     * - A tuple/object containing necessary context
     * 
     * @param target the target object for the tool operation
     * @throws IllegalArgumentException if target is null
     */
    public abstract void useTool(T target);
    
    /**
     * Gets the name of this tool for display purposes.
     * 
     * @return the tool name (e.g., "PlusShapeStamp", "BoxFlipper")
     */
    public abstract String getToolName();
    
    /**
     * Validates that a letter is within the valid range (A-H).
     * This is a utility method for tools that stamp letters.
     * 
     * @param letter the letter to validate
     * @return true if valid (A-H), false otherwise
     */
    protected boolean isValidLetter(char letter) {
        return letter >= MIN_LETTER && letter <= MAX_LETTER;
    }
    
    /**
     * Validates that a row index is within valid bounds (0-7).
     * 
     * @param row the row index to validate
     * @return true if valid (0-7), false otherwise
     */
    protected boolean isValidRow(int row) {
        return row >= MIN_INDEX && row <= MAX_INDEX;
    }
    
    /**
     * Validates that a column index is within valid bounds (0-7).
     * 
     * @param col the column index to validate
     * @return true if valid (0-7), false otherwise
     */
    protected boolean isValidColumn(int col) {
        return col >= MIN_INDEX && col <= MAX_INDEX;
    }
    
    /**
     * Validates that both row and column indices are within valid bounds.
     * 
     * @param row the row index
     * @param col the column index
     * @return true if both are valid, false otherwise
     */
    protected boolean isValidPosition(int row, int col) {
        return isValidRow(row) && isValidColumn(col);
    }
    
    /**
     * Provides a string representation of the tool.
     * 
     * @return the tool name
     */
    @Override
    public String toString() {
        return getToolName();
    }
    
    /**
     * Checks equality based on tool type.
     * Two tools are equal if they are of the same class.
     * 
     * @param obj the object to compare
     * @return true if the objects are of the same tool type
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return this.getClass() == obj.getClass();
    }
    
    /**
     * Returns a hash code based on the tool class name.
     * 
     * @return hash code of the tool class
     */
    @Override
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }
}
