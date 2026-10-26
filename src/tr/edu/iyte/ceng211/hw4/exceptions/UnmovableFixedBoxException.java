package tr.edu.iyte.ceng211.hw4.exceptions;

/**
 * Exception thrown when attempting to move or manipulate a FixedBox.
 * 
 * This exception is thrown in two scenarios:
 * 1. When a FixedBox on the edge is selected to be rolled (Stage 1)
 * 2. When attempting to flip a FixedBox using BoxFlipper (Stage 2)
 * 
 * When this exception is thrown, the current turn is wasted.
 * 
 * Design rationale: This is a RuntimeException (unchecked) because:
 * - It represents a game rule violation that should be caught and handled gracefully
 * - The game should never terminate due to this exception
 * - It allows for cleaner code in the game flow without forced try-catch blocks everywhere
 */
public class UnmovableFixedBoxException extends RuntimeException {
    
    // Default error message
    private static final String DEFAULT_MESSAGE = "Cannot move or manipulate a FixedBox!";
    
    /**
     * Constructs an UnmovableFixedBoxException with the default message.
     */
    public UnmovableFixedBoxException() {
        super(DEFAULT_MESSAGE);
    }
    
    /**
     * Constructs an UnmovableFixedBoxException with a detailed message.
     * 
     * @param message the detail message explaining the exception
     */
    public UnmovableFixedBoxException(String message) {
        super(message != null && !message.trim().isEmpty() ? message : DEFAULT_MESSAGE);
    }
    
    /**
     * Constructs an UnmovableFixedBoxException with a detailed message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public UnmovableFixedBoxException(String message, Throwable cause) {
        super(message != null && !message.trim().isEmpty() ? message : DEFAULT_MESSAGE, cause);
    }
    
    /**
     * Constructs an UnmovableFixedBoxException with a cause.
     * 
     * @param cause the cause of this exception
     */
    public UnmovableFixedBoxException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
    
    /**
     * Constructs an UnmovableFixedBoxException with position information.
     * This is useful for providing clear feedback to the player.
     * 
     * @param row the row of the unmovable box (0-7)
     * @param col the column of the unmovable box (0-7)
     */
    public UnmovableFixedBoxException(int row, int col) {
        super(String.format("Cannot move FixedBox at position R%d-C%d!", row + 1, col + 1));
    }
    
    /**
     * Constructs an UnmovableFixedBoxException with position and operation information.
     * 
     * @param row the row of the unmovable box (0-7)
     * @param col the column of the unmovable box (0-7)
     * @param operation the operation that was attempted (e.g., "roll", "flip")
     */
    public UnmovableFixedBoxException(int row, int col, String operation) {
        super(String.format("Cannot %s FixedBox at position R%d-C%d!", 
            operation != null ? operation : "manipulate", row + 1, col + 1));
    }
}
