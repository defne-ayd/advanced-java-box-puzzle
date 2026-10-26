package tr.edu.iyte.ceng211.hw4.exceptions;

/**
 * Exception thrown when attempting to fix an already FixedBox using BoxFixer.
 * 
 * This exception is thrown during Stage 2 of a turn when:
 * - A BoxFixer tool is acquired
 * - The player selects a box to fix
 * - The selected box is already a FixedBox
 * 
 * When this exception is thrown, the current turn is wasted.
 * 
 * Design rationale:
 * - This exception prevents redundant fixing operations
 * - Serves as a penalty for poor strategic choices
 * - Encourages players to plan their moves carefully
 * - RuntimeException (unchecked) for graceful game flow
 */
public class BoxAlreadyFixedException extends RuntimeException {
    
    // Default error message
    private static final String DEFAULT_MESSAGE = "The selected box is already a FixedBox!";
    
    /**
     * Constructs a BoxAlreadyFixedException with the default message.
     */
    public BoxAlreadyFixedException() {
        super(DEFAULT_MESSAGE);
    }
    
    /**
     * Constructs a BoxAlreadyFixedException with a detailed message.
     * 
     * @param message the detail message explaining the exception
     */
    public BoxAlreadyFixedException(String message) {
        super(message != null && !message.trim().isEmpty() ? message : DEFAULT_MESSAGE);
    }
    
    /**
     * Constructs a BoxAlreadyFixedException with a detailed message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public BoxAlreadyFixedException(String message, Throwable cause) {
        super(message != null && !message.trim().isEmpty() ? message : DEFAULT_MESSAGE, cause);
    }
    
    /**
     * Constructs a BoxAlreadyFixedException with a cause.
     * 
     * @param cause the cause of this exception
     */
    public BoxAlreadyFixedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
    
    /**
     * Constructs a BoxAlreadyFixedException with position information.
     * This is useful for providing clear feedback to the player.
     * 
     * @param row the row of the already-fixed box (0-7)
     * @param col the column of the already-fixed box (0-7)
     */
    public BoxAlreadyFixedException(int row, int col) {
        super(String.format("The box at position R%d-C%d is already a FixedBox!", row + 1, col + 1));
    }
    
    /**
     * Constructs a BoxAlreadyFixedException with detailed position and context information.
     * 
     * @param row the row of the already-fixed box (0-7)
     * @param col the column of the already-fixed box (0-7)
     * @param additionalInfo additional context about the error
     */
    public BoxAlreadyFixedException(int row, int col, String additionalInfo) {
        super(String.format("The box at position R%d-C%d is already a FixedBox! %s", 
            row + 1, col + 1, 
            additionalInfo != null ? additionalInfo : ""));
    }
    
    /**
     * Factory method for creating a BoxAlreadyFixedException with a helpful suggestion.
     * 
     * @param row the row of the already-fixed box (0-7)
     * @param col the column of the already-fixed box (0-7)
     * @return a new BoxAlreadyFixedException with a suggestion message
     */
    public static BoxAlreadyFixedException withSuggestion(int row, int col) {
        return new BoxAlreadyFixedException(row, col, 
            "Try selecting a different box that is not already fixed.");
    }
}
