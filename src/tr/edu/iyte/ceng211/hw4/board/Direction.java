package tr.edu.iyte.ceng211.hw4.board;

/**
 * Represents the four cardinal directions for movement within the BoxGrid.
 * This Enum stores "delta" values (coordinate changes) for each direction.
 * This allows the Grid to calculate neighbor coordinates mathematically 
 */
enum Direction {

    /** Move Up: Row index decreases by 1, Column stays same. */
    UP(-1, 0),

    /** Move Down: Row index increases by 1, Column stays same. */
    DOWN(1, 0),

    /** Move Left: Row stays same, Column index decreases by 1. */
    LEFT(0, -1),

    /** Move Right: Row stays same, Column index increases by 1. */
    RIGHT(0, 1);

    private int deltaRow;
    private int deltaCol;

    /**
     * Constructor for the Direction enum.
     * @param deltaRow The integer change to the row coordinate (-1, 0, or 1).
     * @param deltaCol The integer change to the column coordinate (-1, 0, or 1).
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Gets the column change associated with this direction.
     * @return -1 for LEFT, 1 for RIGHT, 0 otherwise.
     */
    public int getDeltaCol() {
        return deltaCol;
    }

    /**
     * Gets the row change associated with this direction.
     * @return -1 for UP, 1 for DOWN, 0 otherwise.
     */
    public int getDeltaRow() {
        return deltaRow;
    }
}