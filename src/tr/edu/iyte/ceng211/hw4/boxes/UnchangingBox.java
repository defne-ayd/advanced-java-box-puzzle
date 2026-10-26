package tr.edu.iyte.ceng211.hw4.boxes;

/**
 * UnchangingBox is a special box that cannot be re-stamped.
 * 
 * Properties:
 * - Can be rolled normally
 * - Cannot have its letters changed by any SpecialTool
 * - Guaranteed to contain a SpecialTool (100% chance)
 * - Has 10% chance of being generated in the grid
 * - When stamped, the operation appears to work but the letter remains unchanged
 */
public class UnchangingBox extends Box {
    
    /**
     * Constructor for UnchangingBox.
     * Always contains a tool (100% chance).
     */
    public UnchangingBox() {
        super();
        
        // Guaranteed to contain a tool
        this.hasContent = true;
        
        // Content will be set by the grid initializer
        this.content = null;
    }
    
    /**
     * Rolls the box in the specified direction.
     * UnchangingBox can be rolled like a RegularBox.
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
     * UnchangingBox is not fixed.
     * 
     * @return false always
     */
    @Override
    public boolean isFixed() {
        return false;
    }
    
    /**
     * UnchangingBox cannot be re-stamped.
     * 
     * @return true always
     */
    @Override
    public boolean isUnchanging() {
        return true;
    }
    
    /**
     * Gets the type marker for UnchangingBox.
     * 
     * @return 'U' for Unchanging
     */
    @Override
    public char getTypeMarker() {
        return 'U';
    }
    
    /**
     * Attempts to stamp a new letter on the top surface.
     * However, since this is an UnchangingBox, the letter DOES NOT change.
     * The method accepts the request but does nothing to maintain the invariant.
     * 
     * This design allows SpecialTools to "stamp" UnchangingBoxes without throwing
     * exceptions, but the actual letter remains the same as per the requirements.
     * 
     * @param letter the letter that would be stamped (ignored)
     */
    @Override
    public void stampTop(char letter) {
        // Defensive: validate input (even though we won't use it)
        if (letter < 'A' || letter > 'H') {
            throw new IllegalArgumentException("Letter must be between A and H, got: " + letter);
        }
        
        // Do nothing - UnchangingBox cannot be re-stamped
        // The top letter remains unchanged
        // This is intentional as per the homework requirements:
        // "An UnchangingBox can be restamped using relevant SpecialTools directly 
        // or indirectly. However, its top side letter remains the same afterwards."
    }
}