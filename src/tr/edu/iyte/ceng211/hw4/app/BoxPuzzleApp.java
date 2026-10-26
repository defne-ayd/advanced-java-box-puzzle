package tr.edu.iyte.ceng211.hw4.app;

import tr.edu.iyte.ceng211.hw4.game.BoxPuzzle;

/**
 * BoxPuzzleApp is the entry point of the homework application.
 * It only creates the BoxPuzzle controller and starts the game loop.
 */
public class BoxPuzzleApp {

    public static void main(String[] args) {
        BoxPuzzle puzzle = new BoxPuzzle();
        puzzle.play();
    }
}
