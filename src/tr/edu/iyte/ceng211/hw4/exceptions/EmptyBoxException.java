package tr.edu.iyte.ceng211.hw4.exceptions;

/**
 * Exception thrown when a box is opened but contains no SpecialTool.
 * 
 * This exception is thrown during Stage 2 of a turn when:
 * - A box is selected to be opened
 * - The box was rolled during Stage 1
 * - The box is empty (no tool inside)
 * 
 * When this exception is thrown, the current turn is wasted.
 * 
 * Probability of occurrence:
 * - RegularBox: 25% chance of being empty
 * - UnchangingBox: 0% chance (always contains a tool)
 * - FixedBox: 100% chance (never contains a tool)
 * 
 * Design rationale: This is a RuntimeException (unchecked) because:
 * - It represents a game event (bad luck) rather than a programming error
 * - The game should handle it gracefully without terminating
 * - It's part of the game's challenge and strategy
 */
public class EmptyBoxException extends RuntimeException {
    
    // Default error message
    private static final String DEFAULT_MESSAGE = "The selected box is empty!";
    
    /**
     * Constructs an EmptyBoxException with the default message.
     */
    public EmptyBoxException() {
        super(DEFAULT_MESSAGE);
    }
    
    /**
     * Constructs an EmptyBoxException with a detailed message.
     * 
     * @param message the detail message explaining the exception
     */
    public EmptyBoxException(String message) {
        super(message != null && !message.trim().isEmpty() ? message : DEFAULT_MESSAGE);
    }
    
    /**
     * Constructs an EmptyBoxException with a detailed message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public EmptyBoxException(String message, Throwable cause) {
        super(message != null && !message.trim().isEmpty() ? message : DEFAULT_MESSAGE, cause);
    }
    
    /**
     * Constructs an EmptyBoxException with a cause.
     * 
     * @param cause the cause of this exception
     */
    public EmptyBoxException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
    
    /**
     * Constructs an EmptyBoxException with position information.
     * This is useful for providing clear feedback to the player.
     * 
     * @param row the row of the empty box (0-7)
     * @param col the column of the empty box (0-7)
     */
    public EmptyBoxException(int row, int col) {
        super(String.format("The box at position R%d-C%d is empty!", row + 1, col + 1));
    }
    
    /**
     * Constructs an EmptyBoxException with position and box type information.
     * 
     * @param row the row of the empty box (0-7)
     * @param col the column of the empty box (0-7)
     * @param boxType the type of the box (e.g., "RegularBox", "FixedBox")
     */
    public EmptyBoxException(int row, int col, String boxType) {
        super(String.format("The %s at position R%d-C%d is empty!", 
            boxType != null ? boxType : "box", row + 1, col + 1));
    }
    
    /**
     * Factory method for creating an EmptyBoxException for a FixedBox.
     * FixedBoxes are always empty by definition.
     * 
     * @param row the row of the FixedBox (0-7)
     * @param col the column of the FixedBox (0-7)
     * @return a new EmptyBoxException with appropriate message
     */
    public static EmptyBoxException forFixedBox(int row, int col) {
        return new EmptyBoxException(row, col, "FixedBox");
    }
    
    /**
     * Factory method for creating an EmptyBoxException for a RegularBox.
     * RegularBoxes have a 25% chance of being empty.
     * 
     * @param row the row of the RegularBox (0-7)
     * @param col the column of the RegularBox (0-7)
     * @return a new EmptyBoxException with appropriate message
     */
    public static EmptyBoxException forRegularBox(int row, int col) {
        return new EmptyBoxException(row, col, "RegularBox");
    }
}
