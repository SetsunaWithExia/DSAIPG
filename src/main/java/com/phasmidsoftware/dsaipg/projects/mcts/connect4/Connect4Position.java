package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class to represent a Connect 4 board position.
 */
public class Connect4Position implements Position {
    // Board representation, -1 means empty, 0 is player (Yellow), 1 is machine (Red)
    private final int[][] grid;
    // The last player who made a move
    public final int last;
    // Number of pieces placed
    public final int count;
    // Board dimensions
    private final static int rows = 6;
    private final static int cols = 7;

    // Constructor
    Connect4Position(int[][] grid, int count, int last) {
        this.grid = grid;
        this.count = count;
        this.last = last;
    }

    /**
     * Parse a string of X, O, and . to form a Position.
     *
     * @param grid the grid represented as a String.
     * @param last the last player.
     * @return a Position.
     */
    static Connect4Position parsePosition(final String grid, final int last) {
        int[][] matrix = new int[rows][cols];
        int count = 0;
        String[] rowStrings = grid.split("\\n", rows); // Split by \n rows
        for (int i = 0; i < rows; i++) {
            String[] cells = rowStrings[i].split(" ", cols); // Split each row by spaces up to cols count
            for (int j = 0; j < cols; j++) {
                int cell = parseCell(cells[j].trim());
                if (cell >= 0) count++;
                matrix[i][j] = cell;
            }
        }
        return new Connect4Position(matrix, count, last);
    }
    /**
     * Method to parse a single cell.
     *
     * @param cells the String for the cell.
     * @return a number between -1 and one inclusive.
     */
    static int parseCell(String cells) {
        return switch (cells.toUpperCase()) {
            case "O", "0" -> 0;
            case "X", "1" -> 1;
            default -> -1;
        };
    }

    public static int Getmove(Connect4Position prePosition, Connect4Position aftPosition) {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                if(prePosition.grid[row][col]!=aftPosition.grid[row][col]) return col;
        return -1;
    }


    /**
     * Effect a player's move on this Position.
     * For Connect4, a move is specified only by the column (col).
     * The piece will "fall" to the lowest empty position in that column.
     *
     * @param player the player (0: Yellow, 1: Red)
     * @param col the column to drop the piece in.
     * @return the new Position.
     */
    public Connect4Position move(int player, int col) {
        if (full()) throw new RuntimeException("Position is full");
        if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        if (col < 0 || col >= cols) throw new RuntimeException("Column out of bounds: " + col);
        int[][] matrix = copyGrid();
        // Find the lowest empty position in the specified column
        int row = -1;
        for (int i = rows - 1; i >= 0; i--) {
            if (matrix[i][col] < 0) {
                row = i;
                break;
            }
        }
        if (row >= 0) {
            matrix[row][col] = player;
            return new Connect4Position(matrix, count + 1, player);
        }
        throw new RuntimeException("Column is full: " + col);
    }

    /**
     * Method to yield all the possible moves available on this Position.
     * For Connect4, valid moves are columns that aren't full.
     *
     * @return a list of column indices.
     */
    public List<Integer> moves(int player) {
        if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<Integer> result = new ArrayList<>();
        for (int j = 0; j < cols; j++) {
            // Check if the top cell in column is empty
            if (grid[0][j] < 0) {
                result.add(j);
            }
        }
        return result;
    }

    /**
     * @return true if this Position is full.
     */
    boolean full() {
        return count == rows * cols;
    }

    /**
     * Create a deep copy of the grid
     */
    private int[][] copyGrid() {
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            result[i] = Arrays.copyOf(grid[i], cols);
        return result;
    }


    /**
     * Determine if this Position represents a winner.
     *
     * @return an Optional Integer representing the winning player, or empty if no winner.
     */
    public Optional<Integer> winner() {
        if (count >= 7 && fourInARow()) {
            return Optional.of(last);
        }
        return Optional.empty();
    }



    /**
     * Method to determine if there are four in a row (a winning position).
     *
     * @return true if there are four cells in a line that are the same and equal to the last player.
     */
    boolean fourInARow() {
        // Check horizontal
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - 4; j++) {
                if (grid[i][j] != -1 &&
                        grid[i][j] == grid[i][j+1] &&
                        grid[i][j] == grid[i][j+2] &&
                        grid[i][j] == grid[i][j+3]) {
                    return true;
                }
            }
        }
        // Check vertical
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] != -1 &&
                        grid[i][j] == grid[i+1][j] &&
                        grid[i][j] == grid[i+2][j] &&
                        grid[i][j] == grid[i+3][j]) {
                    return true;
                }
            }
        }
        // Check diagonal (down-right)
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 0; j <= cols - 4; j++) {
                if (grid[i][j] != -1 &&
                        grid[i][j] == grid[i+1][j+1] &&
                        grid[i][j] == grid[i+2][j+2] &&
                        grid[i][j] == grid[i+3][j+3]) {
                    return true;
                }
            }
        }
        // Check diagonal (up-right)
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 3; j < cols; j++) {
                if (grid[i][j] != -1 &&
                        grid[i][j] == grid[i+1][j-1] &&
                        grid[i][j] == grid[i+2][j-2] &&
                        grid[i][j] == grid[i+3][j-3]) {
                    return true;
                }
            }
        }
        return false; // No four in a row found
    }



    /**
     * Method to render this Position in a pleasing manner.
     *
     * @return a String.
     */
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(render(grid[i][j]));
                if (j < cols - 1) sb.append(' ');
            }
            if (i < rows - 1) sb.append('\n');
        }

        // Add column numbers at the bottom
        sb.append('\n');
        for (int j = 0; j < cols; j++) {
            sb.append(j);
            if (j < cols - 1) sb.append(' ');
        }

        return sb.toString();
    }

    /**
     * Convert numeric representation to character representation
     */
    private char render(int x) {
        return switch (x) {
            case 0 -> 'O';  // Machine/Yellow
            case 1 -> 'X';  // Human/Red
            default -> '.'; // Empty cell
        };
    }

    /**
     * Get the difference between two positions to determine the move made.
     *
     * @param prevPos the previous position
     * @param currPos the current position
     * @return the column where the move was made
     */
    public static int getLastMoveColumn(Connect4Position prevPos, Connect4Position currPos) {
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                if (prevPos.grid[i][j] != currPos.grid[i][j]) {
                    return j;
                }
            }
        }
        throw new RuntimeException("The positions are identical.");
    }

    /**
     * Create a new empty board position as the initial state for a game
     *
     * @return a new empty board position
     */
    public static Connect4Position emptyPosition() {
        int[][] emptyGrid = new int[rows][cols];
        // Initialize all cells to -1 (empty)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                emptyGrid[i][j] = -1;
            }
        }
        // Initial last is -1 meaning no player has moved yet
        return new Connect4Position(emptyGrid, 0, -1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j]);
                if (j < cols - 1) sb.append(',');
            }
            if (i < rows - 1) sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connect4Position position)) return false;
        return Arrays.deepEquals(grid, position.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }
}