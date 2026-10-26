package tr.edu.iyte.ceng211.hw4.boxes;

/**
 * RegularBox is a standard box in the puzzle game.
 * 
 * Properties:
 * - Can be rolled in any direction
 * - Can be re-stamped by SpecialTools
 * - Has 75% chance of containing a SpecialTool
 * - Has 85% chance of being generated in the grid
 */
public class RegularBox extends Box {
    
    // Probability constants for better maintainability
    private static final int TOOL_PROBABILITY_THRESHOLD = 75;
    
    /**
     * Constructor for RegularBox.
     * Determines if it contains a tool based on probability (75% chance).
     */
    public RegularBox() {
        super();
        
        // Defensive check: ensure random is initialized
        if (random == null) {
            throw new IllegalStateException("Random generator is not initialized");
        }
        
        // 75% chance of containing a tool
        this.hasContent = (random.nextInt(100) < TOOL_PROBABILITY_THRESHOLD);
        
        // Content will be set by the grid initializer
        this.content = null;
    }
    
    /**
     * Rolls the box in the specified direction.
     * This simulates the physical rolling of a die/cube.
     * 
     * @param direction the direction to roll (expects Direction enum from board package)
     * @throws IllegalArgumentException if direction is null or invalid
     */
    @Override
    public void roll(Object direction) {
        // Defensive: validate input
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        
        String dir = direction.toString().toUpperCase();
        
        // Temporary array to hold new surface arrangement
        char[] newSurfaces = new char[6];
        
        switch (dir) {
            case "UP":
                // Front -> Top, Top -> Back, Back -> Bottom, Bottom -> Front
                newSurfaces[0] = surfaces[2]; // Top = old Front
                newSurfaces[1] = surfaces[3]; // Bottom = old Back
                newSurfaces[2] = surfaces[1]; // Front = old Bottom
                newSurfaces[3] = surfaces[0]; // Back = old Top
                newSurfaces[4] = surfaces[4]; // Left stays same
                newSurfaces[5] = surfaces[5]; // Right stays same
                break;
                
            case "DOWN":
                // Top -> Front, Front -> Bottom, Bottom -> Back, Back -> Top
                newSurfaces[0] = surfaces[3]; // Top = old Back
                newSurfaces[1] = surfaces[2]; // Bottom = old Front
                newSurfaces[2] = surfaces[0]; // Front = old Top
                newSurfaces[3] = surfaces[1]; // Back = old Bottom
                newSurfaces[4] = surfaces[4]; // Left stays same
                newSurfaces[5] = surfaces[5]; // Right stays same
                break;
                
            case "LEFT":
                // Right -> Top, Top -> Left, Left -> Bottom, Bottom -> Right
                newSurfaces[0] = surfaces[5]; // Top = old Right
                newSurfaces[1] = surfaces[4]; // Bottom = old Left
                newSurfaces[2] = surfaces[2]; // Front stays same
                newSurfaces[3] = surfaces[3]; // Back stays same
                newSurfaces[4] = surfaces[0]; // Left = old Top
                newSurfaces[5] = surfaces[1]; // Right = old Bottom
                break;
                
            case "RIGHT":
                // Top -> Right, Right -> Bottom, Bottom -> Left, Left -> Top
                newSurfaces[0] = surfaces[4]; // Top = old Left
                newSurfaces[1] = surfaces[5]; // Bottom = old Right
                newSurfaces[2] = surfaces[2]; // Front stays same
                newSurfaces[3] = surfaces[3]; // Back stays same
                newSurfaces[4] = surfaces[1]; // Left = old Bottom
                newSurfaces[5] = surfaces[0]; // Right = old Top
                break;
                
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction + 
                    ". Valid directions are: UP, DOWN, LEFT, RIGHT");
        }
        
        // Apply the new surface arrangement
        this.surfaces = newSurfaces;
        
        // Mark that this box has been rolled this turn
        markAsRolled();
    }
    
    /**
     * RegularBox is not fixed.
     * 
     * @return false always
     */
    @Override
    public boolean isFixed() {
        return false;
    }
    
    /**
     * RegularBox can be re-stamped.
     * 
     * @return false always
     */
    @Override
    public boolean isUnchanging() {
        return false;
    }
    
    /**
     * Gets the type marker for RegularBox.
     * 
     * @return 'R' for Regular
     */
    @Override
    public char getTypeMarker() {
        return 'R';
    }
    
    /**
     * Stamps a new letter on the top surface.
     * RegularBox allows re-stamping freely.
     * 
     * @param letter the new letter to stamp
     * @throws IllegalArgumentException if letter is not between A and H
     */
    @Override
    public void stampTop(char letter) {
        // Defensive: validate input
        if (letter < 'A' || letter > 'H') {
            throw new IllegalArgumentException("Letter must be between A and H, got: " + letter);
        }
        
        surfaces[0] = letter;
    }
}