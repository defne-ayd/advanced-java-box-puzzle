package tr.edu.iyte.ceng211.hw4.boxes;

import java.util.Random;

/**
 * Abstract base class representing a Box in the puzzle game.
 * Each box has 6 surfaces with letters stamped on them.
 * 
 * Surface indexing:
 * 0 - Top
 * 1 - Bottom
 * 2 - Front
 * 3 - Back
 * 4 - Left
 * 5 - Right
 */
public abstract class Box {
    protected char[] surfaces;
    protected boolean hasContent;
    protected Object content;
    protected boolean wasRolledThisTurn;
    protected static final char[] POSSIBLE_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    
    // Random generator for creating box surfaces
    protected static final Random random = new Random();
    
    /**
     * Constructor initializes the 6 surfaces with random valid letters.
     * Rule: No more than 2 of the same letter per box.
     */
    public Box() {
        this.surfaces = new char[6];
        this.wasRolledThisTurn = false;
        generateValidSurfaces();
    }
    
    /**
     * Generates valid surfaces ensuring no letter appears more than twice.
     * This method creates a valid combination of letters for the box surfaces.
     */
    private void generateValidSurfaces() {
        int[] letterCounts = new int[8]; // Count for each letter A-H
        
        for (int i = 0; i < 6; i++) {
            char letter;
            int letterIndex;
            
            // Keep trying until we find a valid letter
            do {
                letter = POSSIBLE_LETTERS[random.nextInt(POSSIBLE_LETTERS.length)];
                letterIndex = letter - 'A'; // Convert to index (A=0, B=1, etc.)
            } while (letterCounts[letterIndex] >= 2); // Ensure no more than 2 occurrences
            
            surfaces[i] = letter;
            letterCounts[letterIndex]++;
        }
    }
    
    /**
     * Gets the letter on the top surface of the box.
     * 
     * @return the character on the top surface
     */
    public char getTopLetter() {
        return surfaces[0];
    }
    
    /**
     * Gets the letter on the bottom surface of the box.
     * 
     * @return the character on the bottom surface
     */
    public char getBottomLetter() {
        return surfaces[1];
    }
    
    /**
     * Gets all 6 surfaces of the box.
     * 
     * @return array of characters representing all surfaces
     */
    public char[] getAllSurfaces() {
        return surfaces.clone(); // Return a copy to prevent external modification
    }
    
    /**
     * Stamps a new letter on the top surface.
     * This method may be overridden by subclasses with special behavior.
     * 
     * @param letter the new letter to stamp on top
     */
    public void stampTop(char letter) {
        surfaces[0] = letter;
    }
    
    /**
     * Flips the box upside down.
     * Top becomes bottom, bottom becomes top.
     */
    public void flipUpsideDown() {
        char temp = surfaces[0];
        surfaces[0] = surfaces[1];
        surfaces[1] = temp;
    }
    
    /**
     * Rolls the box in a specific direction.
     * This simulates the physical rolling of a cube.
     * 
     * Direction mapping:
     * - UP: Front -> Top, Top -> Back, Back -> Bottom, Bottom -> Front
     * - DOWN: Top -> Front, Front -> Bottom, Bottom -> Back, Back -> Top
     * - LEFT: Right -> Top, Top -> Left, Left -> Bottom, Bottom -> Right
     * - RIGHT: Top -> Right, Right -> Bottom, Bottom -> Left, Left -> Top
     * 
     * @param direction the direction to roll (as enum or string)
     */
    public abstract void roll(Object direction);
    
    /**
     * Checks if this box is a FixedBox (cannot be moved).
     * 
     * @return true if this is a FixedBox, false otherwise
     */
    public abstract boolean isFixed();
    
    /**
     * Checks if this box is an UnchangingBox (cannot be re-stamped).
     * 
     * @return true if this is an UnchangingBox, false otherwise
     */
    public abstract boolean isUnchanging();
    
    /**
     * Gets the type marker for display purposes.
     * R for RegularBox, X for FixedBox, U for UnchangingBox.
     * 
     * @return the type marker character
     */
    public abstract char getTypeMarker();
    
    /**
     * Checks if the box has content (tool or empty).
     * 
     * @return true if the box has content, false if empty
     */
    public boolean hasContent() {
        return hasContent;
    }
    
    /**
     * Gets the content of the box.
     * 
     * @return the content object (SpecialTool or null)
     */
    public Object getContent() {
        return content;
    }
    
    /**
     * Sets the content of the box.
     * 
     * @param content the tool to place in the box
     */
    public void setContent(Object content) {
        this.content = content;
        this.hasContent = (content != null);
    }
    
    /**
     * Removes the content from the box (marks it as empty).
     */
    public void removeContent() {
        this.content = null;
        this.hasContent = false;
    }
    
    /**
     * Marks that this box was rolled in the current turn.
     */
    public void markAsRolled() {
        this.wasRolledThisTurn = true;
    }
    
    /**
     * Resets the rolled status for a new turn.
     */
    public void resetRolledStatus() {
        this.wasRolledThisTurn = false;
    }
    
    /**
     * Checks if this box was rolled in the current turn.
     * 
     * @return true if rolled this turn, false otherwise
     */
    public boolean wasRolledThisTurn() {
        return wasRolledThisTurn;
    }
    
    /**
     * Gets the mystery/open marker for display.
     * M if not opened (has potential content), O if opened/empty.
     * 
     * @return the marker character
     */
    public char getContentMarker() {
        return hasContent ? 'M' : 'O';
    }
    
    /**
     * Creates a string representation for the grid display.
     * Format: | TYPE-LETTER-CONTENT |
     * Example: | R-A-M | or | X-B-O |
     * 
     * @return formatted string for display
     */
    @Override
    public String toString() {
        return " " + getTypeMarker() + "-" + getTopLetter() + "-" + getContentMarker() + " |";
    }

}
