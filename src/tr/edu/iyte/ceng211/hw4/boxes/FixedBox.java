package tr.edu.iyte.ceng211.hw4.boxes;

import tr.edu.iyte.ceng211.hw4.exceptions.UnmovableFixedBoxException;

/**
 * FixedBox is a special box that cannot be moved or rolled.
 * 
 * Properties:
 * - Cannot be rolled in any direction
 * - Blocks the domino effect (stops rolling propagation)
 * - Never contains a SpecialTool (always empty)
 * - Has 5% chance of being generated in the grid
 * - Attempting to roll it throws UnmovableFixedBoxException
 */
public class FixedBox extends Box {
    
    /**
     * Constructor for FixedBox.
     * Never contains a tool (0% chance).
     */
    public FixedBox() {
        super();
        
        // Never contains a tool - always empty
        this.hasContent = false;
        this.content = null;
    }
    
    /**
     * Attempts to roll the box.
     * Since FixedBox cannot be moved, this always throws an exception.
     * 
     * @param direction the direction attempted (ignored)
     * @throws UnmovableFixedBoxException always thrown for FixedBox
     */
    @Override
    public void roll(Object direction) {
        // Defensive: validate input before throwing exception
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        
        // FixedBox cannot be rolled - throw exception
        throw new UnmovableFixedBoxException("FixedBox cannot be moved or rolled in direction: " + direction);
    }
    
    /**
     * FixedBox is indeed fixed.
     * 
     * @return true always
     */
    @Override
    public boolean isFixed() {
        return true;
    }
    
    /**
     * FixedBox is not an UnchangingBox (different type).
     * Although it also cannot be changed, it's a separate category.
     * 
     * @return false always
     */
    @Override
    public boolean isUnchanging() {
        return false;
    }
    
    /**
     * Gets the type marker for FixedBox.
     * 
     * @return 'X' for fiXed
     */
    @Override
    public char getTypeMarker() {
        return 'X';
    }
    
    /**
     * Attempts to stamp a new letter on the top surface.
     * FixedBox technically can have its top stamped (though this shouldn't happen in normal gameplay).
     * Since it never moves, the practical effect is minimal.
     * 
     * However, for defensive coding, we allow stamping to prevent crashes
     * if a tool mistakenly tries to stamp it.
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
        
        // Allow stamping for safety, though it shouldn't happen in normal gameplay
        surfaces[0] = letter;
    }
    
    /**
     * Flipping a FixedBox is not allowed in the game logic.
     * This override prevents accidental flipping.
     * 
     * @throws UnmovableFixedBoxException when attempting to flip
     */
    @Override
    public void flipUpsideDown() {
        throw new UnmovableFixedBoxException("FixedBox cannot be flipped upside down!");
    }
    
    /**
     * FixedBox never has content, so this always returns false.
     * 
     * @return false always
     */
    @Override
    public boolean hasContent() {
        return false;
    }
    
    /**
     * FixedBox content is always null.
     * 
     * @return null always
     */
    @Override
    public Object getContent() {
        return null;
    }
    
    /**
     * Prevents setting content on a FixedBox.
     * This is a defensive measure to maintain the invariant.
     * Silently fails to prevent crashes if accidentally called.
     * 
     * @param content ignored
     */
    @Override
    public void setContent(Object content) {
        // Defensive: Do nothing - FixedBox can never have content
        // Silent failure to prevent crashes
        // This maintains the class invariant: FixedBox.hasContent is always false
    }
    
    /**
     * FixedBox content cannot be removed since it never has any.
     * This override is defensive programming.
     */
    @Override
    public void removeContent() {
        // Defensive: Do nothing - already always empty
        // This maintains consistency with setContent behavior
    }
    
    /**
     * FixedBox is always marked as empty (O).
     * 
     * @return 'O' always
     */
    @Override
    public char getContentMarker() {
        return 'O'; // Always empty
    }
}