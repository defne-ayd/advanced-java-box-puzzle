package tr.edu.iyte.ceng211.hw4.game;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import tr.edu.iyte.ceng211.hw4.board.BoxGrid;
import tr.edu.iyte.ceng211.hw4.boxes.Box;
import tr.edu.iyte.ceng211.hw4.boxes.FixedBox;
import tr.edu.iyte.ceng211.hw4.exceptions.BoxAlreadyFixedException;
import tr.edu.iyte.ceng211.hw4.exceptions.EmptyBoxException;
import tr.edu.iyte.ceng211.hw4.exceptions.UnmovableFixedBoxException;
import tr.edu.iyte.ceng211.hw4.tools.*;

/**
 * BoxPuzzle is the main controller class for the game.
 * My responsibilities are:
 * - run the 5-turn game loop
 * - handle user input + validation (Menu)
 * - catch exceptions and print correct messages
 * I use reflection to:
 * - create Direction values (UP/DOWN/LEFT/RIGHT)
 * - call BoxGrid.roll(int,int,Direction)
 */
public class BoxPuzzle {

    private static final int GRID_SIZE = 8;
    private static final int MAX_TURNS = 5;

    private final BoxGrid grid;
    private final char targetLetter;
    private final Scanner scanner;
    private final Menu menu;
    private final Random random;

    /**
     * ToolContext exists only so I can pass "some non-null contextObject"
     * into useTool(T target) method.
    **/
    private static class ToolContext {
        final BoxGrid grid;
        final char targetLetter;

        ToolContext(BoxGrid grid, char targetLetter) {
            this.grid = grid;
            this.targetLetter = targetLetter;
        }
    }

    public BoxPuzzle() {
        this.grid = new BoxGrid();
        this.scanner = new Scanner(System.in);
        this.menu = new Menu(grid, scanner);
        this.random = new Random();

        // Target letter is chosen randomly A-H.
        this.targetLetter = (char) ('A' + random.nextInt(8));

        assignToolsIntoBoxes();
    }

    /**
     * Runs the full game: 5 turns, each has 2 stages.
     */
    public void play() {
        System.out.println("Welcome to Box Top Side Matching Puzzle App. An 8x8 box grid is being generated.");
        System.out.println("Your goal is to maximize the letter \"" + targetLetter + "\" on the top sides of the boxes.");
        System.out.println("The initial state of the box grid:");
        grid.printGrid();

        boolean failedEarly = false;

        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            System.out.println("=====> TURN " + turn + ":");

            // Optional view box surfaces
            menu.handleViewSurfaces();

            // If all edge boxes are fixed, no move can be made (failure condition).
            if (allEdgeBoxesFixed()) {
                failedEarly = true;
                break;
            }

            // TURN STAGE 1: choose edge + roll
            System.out.println("---> TURN " + turn + " - FIRST STAGE:");

            int[] edge = menu.getEdgeBoxPosition();
            int startR = edge[0];
            int startC = edge[1];

            // Decides direction rules (corners allow 2 choices)
            String dirName = menu.getDirectionNameForRoll(startR, startC);

            // Stage 2 needs "boxes rolled in stage 1",
            // using the same scan idea: rolled boxes end up in cells from (start+delta) until before limit.
            List<int[]> affectedPositions = computeAffectedPositions(startR, startC, dirName);

            try {
                rollViaReflection(startR, startC, dirName);

                String msg = "The chosen box and any box on its path have been rolled " + prettyDir(dirName) + ".";
                if (affectedPositions.size() < GRID_SIZE - 1) {
                    msg += " until a FixedBox has been reached.";
                }
                System.out.println(msg);

                System.out.println("The new state of the box grid:");
                grid.printGrid();

            } catch (UnmovableFixedBoxException e) {
                System.out.println("HOWEVER, IT IS FIXED BOX AND CANNOT BE MOVED. Continuing to the next turn...");
                continue; // turn wasted
            } catch (RuntimeException e) {
                // Defensive: if reflection fails for any reason
                System.out.println("An internal error occurred during rolling. Continuing to the next turn...");
                continue;
            }

            if (affectedPositions.isEmpty()) {
                System.out.println("No boxes were rolled in this move. Continuing to the next turn...");
                continue;
            }

            // TURN STAGE 2: open + use tool
            System.out.println("---> TURN " + turn + " – SECOND STAGE:");

            int[] openPos = menu.getRolledBoxPosition(affectedPositions);
            int openR = openPos[0];
            int openC = openPos[1];

            Box chosen = grid.getBox(openR, openC);

            SpecialTool<?> tool;
            try {
                // Empty box rule: if empty -> exception -> turn wasted.
                if (!chosen.hasContent() || chosen.getContent() == null) {
                    throw new EmptyBoxException(openR, openC);
                }

                tool = (SpecialTool<?>) chosen.getContent();
                chosen.removeContent();

                System.out.println("The box on location R" + (openR + 1) + "-C" + (openC + 1)
                        + " is opened. It contains a SpecialTool --> " + tool.getToolName());

            } catch (EmptyBoxException e) {
                System.out.println("BOX IS EMPTY! Continuing to the next turn...");
                continue;
            } catch (ClassCastException e) {
                // Defensive: if something weird is stored inside content
                System.out.println("Invalid content found in the box. Continuing to the next turn...");
                continue;
            }

            // Apply tool
            boolean ok = applyTool(tool);
            if (ok) {
                System.out.println("The new state of the box grid:");
                grid.printGrid();
            }
        }

        // Game Over output
        System.out.println("******** GAME OVER ********");
        System.out.println("The final state of the box grid:");
        grid.printGrid();

        int count = countTargetOnTop();
        System.out.println("THE TOTAL NUMBER OF TARGET LETTER \"" + targetLetter + "\" IN THE BOX GRID --> " + count);

        if (failedEarly) {
            System.out.println("FAILURE: No more moves can be made.");
        } else {
            System.out.println("The game has been SUCCESSFULLY completed!");
        }
    }

    // Tool usage

    private boolean applyTool(SpecialTool<?> rawTool) {
        try {
            // Because rawTool is SpecialTool<?>, I cast to SpecialTool<Object> to pass ToolContext.
            ToolContext ctx = new ToolContext(grid, targetLetter);

            @SuppressWarnings("unchecked")
            SpecialTool<Object> tool = (SpecialTool<Object>) rawTool;
            tool.useTool(ctx);

            String toolName = rawTool.getToolName();

            if ("PlusShapeStamp".equals(toolName)) {
                @SuppressWarnings("unchecked")
                PlusShapeStamp<Object> ps = (PlusShapeStamp<Object>) rawTool;

                int[] center = menu.getAnyBoxPosition("Please enter the center location for the PlusShapeStamp: ");
                int r = center[0], c = center[1];

                int[][] affected = ps.getAffectedPositions(r, c);
                for (int[] p : affected) {
                    grid.getBox(p[0], p[1]).stampTop(targetLetter);
                }

                System.out.println("The PlusShapeStamp has been used on center R" + (r + 1) + "-C" + (c + 1)
                        + ". The affected boxes have been stamped with " + targetLetter + ".");

            } else if ("MassRowStamp".equals(toolName)) {
                @SuppressWarnings("unchecked")
                MassRowStamp<Object> rs = (MassRowStamp<Object>) rawTool;

                int rowIndex = menu.getRowIndex("Please enter the row number (1-8) for the MassRowStamp: ");
                int[][] affected = rs.getAffectedPositions(rowIndex);

                for (int[] p : affected) {
                    grid.getBox(p[0], p[1]).stampTop(targetLetter);
                }

                System.out.println("The MassRowStamp has been used on row " + (rowIndex + 1)
                        + ". The affected boxes have been stamped with " + targetLetter + ".");

            } else if ("MassColumnStamp".equals(toolName)) {
                @SuppressWarnings("unchecked")
                MassColumnStamp<Object> cs = (MassColumnStamp<Object>) rawTool;

                int colIndex = menu.getColIndex("Please enter the column number (1-8) for the MassColumnStamp: ");
                int[][] affected = cs.getAffectedPositions(colIndex);

                for (int[] p : affected) {
                    grid.getBox(p[0], p[1]).stampTop(targetLetter);
                }

                System.out.println("The MassColumnStamp has been used on column " + (colIndex + 1)
                        + ". The affected boxes have been stamped with " + targetLetter + ".");

            } else if ("BoxFlipper".equals(toolName)) {
                // Flips a single chosen box upside down
                int[] rc = menu.getAnyBoxPosition("Please enter the location of the box you want to flip: ");
                int r = rc[0], c = rc[1];

                grid.getBox(r, c).flipUpsideDown();

                System.out.println("The BoxFlipper has been used on R" + (r + 1) + "-C" + (c + 1)
                        + ". The box has been flipped upside down.");

            } else if ("BoxFixer".equals(toolName)) {
                // Replaces a box with an identical FixedBox copy
                int[] rc = menu.getAnyBoxPosition("Please enter the location of the box you want to fix: ");
                int r = rc[0], c = rc[1];

                Box b = grid.getBox(r, c);
                if (b.isFixed()) {
                    throw new BoxAlreadyFixedException(r, c);
                }

                FixedBox copy = new FixedBox();

                // Box has no setter for surfaces.
                // So I copy the surfaces via reflection to satisfy "identical FixedBox copy".
                copySurfacesViaReflection(b, copy);

                // FixedBox never contains tools by design, so we don't copy content.
                grid.setBox(r, c, copy);

                System.out.println("The BoxFixer has been used on R" + (r + 1) + "-C" + (c + 1)
                        + ". The box is now replaced with an identical FixedBox.");

            } else {
                System.out.println("Unknown tool: " + toolName + ". The turn is wasted.");
                return false;
            }

            return true;

        } catch (UnmovableFixedBoxException | BoxAlreadyFixedException e) {
            System.out.println(e.getMessage() + " The turn is wasted.");
            return false;
        } catch (IllegalArgumentException e) {
            // Invalid row/col/letter usage
            System.out.println("INCORRECT INPUT: " + e.getMessage() + " The turn is wasted.");
            return false;
        } catch (Exception e) {
            System.out.println("An error occurred while using the tool. The turn is wasted.");
            return false;
        }
    }

    // Rolling via reflection (Direction is package-private)

    private void rollViaReflection(int row, int col, String dirName) {
        try {
            Class<?> dirClass = Class.forName("tr.edu.iyte.ceng211.hw4.board.Direction");
            @SuppressWarnings("unchecked")
            Object dirEnum = Enum.valueOf((Class<? extends Enum>) dirClass, dirName);

            Method rollMethod = BoxGrid.class.getMethod("roll", int.class, int.class, dirClass);
            rollMethod.invoke(grid, row, col, dirEnum);

        } catch (Exception e) {
            // If the underlying method threw UnmovableFixedBoxException, it will be wrapped.
            Throwable cause = e.getCause();
            if (cause instanceof UnmovableFixedBoxException) {
                throw (UnmovableFixedBoxException) cause;
            }
            throw new RuntimeException("Reflection roll failed", e);
        }
    }

    /**
     * Computes the list of positions that can be opened in Stage 2.
     * These are the destination cells that receive rolled boxes after Stage 1.
     */
    private List<int[]> computeAffectedPositions(int startR, int startC, String dirName) {
        // Convert dirName to delta
        int dr = 0, dc = 0;
        switch (dirName) {
            case "UP": dr = -1; dc = 0; break;
            case "DOWN": dr = 1; dc = 0; break;
            case "LEFT": dr = 0; dc = -1; break;
            case "RIGHT": dr = 0; dc = 1; break;
        }

        // Scan forward until edge or a FixedBox
        int scanR = startR;
        int scanC = startC;

        int limitR;
        int limitC;

        while (true) {
            int nextR = scanR + dr;
            int nextC = scanC + dc;

            if (nextR < 0 || nextR >= GRID_SIZE || nextC < 0 || nextC >= GRID_SIZE) {
                limitR = nextR; // outside
                limitC = nextC;
                break;
            }

            Box nextBox = grid.getBox(nextR, nextC);
            if (nextBox != null && nextBox.isFixed()) {
                limitR = nextR; // fixed cell
                limitC = nextC;
                break;
            }

            scanR = nextR;
            scanC = nextC;
        }

        // The last destination cell is the cell before the limit
        int lastDestR = limitR - dr;
        int lastDestC = limitC - dc;

        // Now build destination positions: start+delta ... lastDest
        List<int[]> affected = new ArrayList<>();

        int curR = startR + dr;
        int curC = startC + dc;

        while (curR >= 0 && curR < GRID_SIZE && curC >= 0 && curC < GRID_SIZE) {
            // stop if we reached the fixed wall cell (limit inside) -> lastDest already avoids it
            affected.add(new int[]{curR, curC});
            if (curR == lastDestR && curC == lastDestC) {
                break;
            }
            curR += dr;
            curC += dc;
        }

        // If the wall is adjacent, affected list becomes empty (valid outcome).
        if (!affected.isEmpty()) {
            // If lastDest equals start, our loop will still add something incorrectly in some corner cases.
            // This defensive check keeps it safe.
            if (lastDestR == startR && lastDestC == startC) {
                affected.clear();
            }
        }

        return affected;
    }

    // Tool distribution

    private void assignToolsIntoBoxes() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Box b = grid.getBox(r, c);
                if (b == null) continue;

                // FixedBoxes never contain tools
                if (b.isFixed()) {
                    b.removeContent(); // defensive
                    continue;
                }

                // If box is supposed to have content (Regular 75%, Unchanging 100%)
                // then I place a random tool (uniform among 5 tools).
                if (b.hasContent()) {
                    b.setContent(randomTool());
                } else {
                    // If hasContent is false, ensure content is null.
                    b.removeContent();
                }
            }
        }
    }

    private SpecialTool<?> randomTool() {
        int pick = random.nextInt(5);
        switch (pick) {
            case 0: return new PlusShapeStamp<>();
            case 1: return new MassRowStamp<>();
            case 2: return new MassColumnStamp<>();
            case 3: return new BoxFlipper<>();
            default: return new BoxFixer<>();
        }
    }

    // Helpers (count + early-failure + copy surfaces)

    private int countTargetOnTop() {
        int count = 0;
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (grid.getBox(r, c).getTopLetter() == targetLetter) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean allEdgeBoxesFixed() {
        // Top + bottom rows
        for (int c = 0; c < GRID_SIZE; c++) {
            if (!grid.getBox(0, c).isFixed()) return false;
            if (!grid.getBox(GRID_SIZE - 1, c).isFixed()) return false;
        }
        // Left + right columns (excluding corners already checked)
        for (int r = 1; r < GRID_SIZE - 1; r++) {
            if (!grid.getBox(r, 0).isFixed()) return false;
            if (!grid.getBox(r, GRID_SIZE - 1).isFixed()) return false;
        }
        return true;
    }

    private void copySurfacesViaReflection(Box from, Box to) {
        try {
            Field surfacesField = Box.class.getDeclaredField("surfaces");
            surfacesField.setAccessible(true);

            char[] src = (char[]) surfacesField.get(from);
            char[] clone = src.clone();

            surfacesField.set(to, clone);

        } catch (Exception e) {
            // If this fails, I still want the program to run,
            // so I at least preserve the top letter.
            to.stampTop(from.getTopLetter());
        }
    }

    private String prettyDir(String dirName) {
        switch (dirName) {
            case "UP": return "upwards";
            case "DOWN": return "downwards";
            case "LEFT": return "left";
            case "RIGHT": return "right";
            default: return dirName.toLowerCase(Locale.ROOT);
        }
    }

    // Inner Menu Class

    private static class Menu {
        private final BoxGrid grid;
        private final Scanner sc;

        Menu(BoxGrid grid, Scanner sc) {
            this.grid = grid;
            this.sc = sc;
        }

        void handleViewSurfaces() {
            System.out.print("---> Do you want to view all surfaces of a box? [1] Yes or [2] No? ");
            int choice = readInt(1, 2);

            if (choice == 2) {
                System.out.println("Continuing to the first stage...");
                return;
            }

            int[] pos = readLocation("Please enter the location of the box you want to view: ");
            Box box = grid.getBox(pos[0], pos[1]);
            char[] s = box.getAllSurfaces();

            System.out.println("    | " + s[3] + " | "); // Back
            System.out.println("-------------");
            System.out.println("| " + s[4] + " | " + s[0] + " | " + s[5] + " | "); // Left, Top, Right
            System.out.println("-------------");
            System.out.println("    | " + s[2] + " | "); // Front
            System.out.println("    | " + s[1] + " | "); // Bottom

            System.out.println("Continuing to the first stage...");
        }

        int[] getEdgeBoxPosition() {
            while (true) {
                int[] pos = readLocation("Please enter the location of the edge box you want to roll: ");
                int r = pos[0], c = pos[1];
                if (r == 0 || r == 7 || c == 0 || c == 7) return pos;

                System.out.println("INCORRECT INPUT: The chosen box is not on any of the edges. Please reenter the location: ");
            }
        }

        /**
         * Returns the direction NAME as a String (UP/DOWN/LEFT/RIGHT).
         */
        String getDirectionNameForRoll(int row, int col) {
            boolean corner = (row == 0 || row == 7) && (col == 0 || col == 7);

            if (!corner) {
                if (row == 0) {
                    System.out.println("The chosen box is automatically rolled downwards.");
                    return "DOWN";
                }
                if (row == 7) {
                    System.out.println("The chosen box is automatically rolled upwards.");
                    return "UP";
                }
                if (col == 0) {
                    System.out.println("The chosen box is automatically rolled to right.");
                    return "RIGHT";
                }
                System.out.println("The chosen box is automatically rolled to left.");
                return "LEFT";
            }

            String aTxt, bTxt;
            String a, b;

            if (row == 0 && col == 0) {
                a = "RIGHT"; b = "DOWN"; aTxt = "right"; bTxt = "downwards";
            } else if (row == 0 && col == 7) {
                a = "LEFT"; b = "DOWN"; aTxt = "left"; bTxt = "downwards";
            } else if (row == 7 && col == 0) {
                a = "RIGHT"; b = "UP"; aTxt = "right"; bTxt = "upwards";
            } else {
                a = "LEFT"; b = "UP"; aTxt = "left"; bTxt = "upwards";
            }

            System.out.print("The chosen box can be rolled to either [1] " + aTxt + " or [2] " + bTxt + ": ");
            int choice = readInt(1, 2);
            return (choice == 1) ? a : b;
        }

        int[] getRolledBoxPosition(List<int[]> affected) {
            while (true) {
                int[] pos = readLocation("Please enter the location of the box you want to open: ");
                for (int[] a : affected) {
                    if (a[0] == pos[0] && a[1] == pos[1]) {
                        return pos;
                    }
                }
                System.out.println("INCORRECT INPUT: The chosen box was not rolled during the first stage. Please reenter the location: ");
            }
        }

        int[] getAnyBoxPosition(String prompt) {
            return readLocation(prompt);
        }

        int getRowIndex(String prompt) {
            System.out.print(prompt);
            return readInt(1, 8) - 1;
        }

        int getColIndex(String prompt) {
            System.out.print(prompt);
            return readInt(1, 8) - 1;
        }

        private int readInt(int min, int max) {
            while (true) {
                try {
                    int x = Integer.parseInt(sc.nextLine().trim());
                    if (x >= min && x <= max) return x;
                } catch (NumberFormatException ignored) {
                }
                System.out.print("Invalid input. Please enter a number between " + min + " and " + max + ": ");
            }
        }

        private int[] readLocation(String prompt) {
            while (true) {
                System.out.print(prompt);
                String input = sc.nextLine().trim().toUpperCase(Locale.ROOT);

                // Accept R1-C1 format
                if (input.matches("R[1-8]-C[1-8]")) {
                    int r = input.charAt(1) - '1';
                    int c = input.charAt(4) - '1';
                    return new int[]{r, c};
                }

                // Accept 1-1 format
                if (input.matches("[1-8]-[1-8]")) {
                    int r = input.charAt(0) - '1';
                    int c = input.charAt(2) - '1';
                    return new int[]{r, c};
                }

                System.out.println("INCORRECT INPUT: Invalid location format.");
            }
        }
    }
}
