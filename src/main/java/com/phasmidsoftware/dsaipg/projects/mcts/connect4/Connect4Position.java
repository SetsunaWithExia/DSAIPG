package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class to represent a Connect 4 board position.
 */
public class Connect4Position {

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
        String[] rowStrings = grid.split("\\n", rows);
        for (int i = 0; i < rows; i++) {
            String[] cells = rowStrings[i].split(" ", cols);
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
     * @param cell the String for the cell.
     * @return a number between -1 and one inclusive.
     */
    static int parseCell(String cell) {
        return switch (cell.toUpperCase()) {
            case "O", "0" -> 0;
            case "X", "1" -> 1;
            default -> -1;
        };
    }

    /**
     * Effect a player's move on this Position.
     * For Connect4, a move is specified only by the column (y).
     * The piece will "fall" to the lowest empty position in that column.
     *
     * @param player the player (0: O, 1: X)
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
     * Determine if this Position represents a winner.
     *
     * @return an Optional Integer representing the winning player, or empty if no winner.
     */
    public Optional<Integer> winner() {
        // Check for four in a row
        if (count >= 7 && fourInARow()) { // Minimum 7 pieces needed for a win
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
                if (grid[i][j] == last && grid[i][j+1] == last &&
                        grid[i][j+2] == last && grid[i][j+3] == last) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == last && grid[i+1][j] == last &&
                        grid[i+2][j] == last && grid[i+3][j] == last) {
                    return true;
                }
            }
        }

        // Check diagonal (down-right)
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 0; j <= cols - 4; j++) {
                if (grid[i][j] == last && grid[i+1][j+1] == last &&
                        grid[i+2][j+2] == last && grid[i+3][j+3] == last) {
                    return true;
                }
            }
        }

        // Check diagonal (up-right)
        for (int i = 3; i < rows; i++) {
            for (int j = 0; j <= cols - 4; j++) {
                if (grid[i][j] == last && grid[i-1][j+1] == last &&
                        grid[i-2][j+2] == last && grid[i-3][j+3] == last) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return true if this Position is full.
     */
    boolean full() {
        return count == rows * cols;
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

    Connect4Position(int[][] grid, int count, int last) {
        this.grid = grid;
        this.count = count;
        this.last = last;
    }

    private int[][] copyGrid() {
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            result[i] = Arrays.copyOf(grid[i], cols);
        return result;
    }

    private char render(int x) {
        return switch (x) {
            case 0 -> 'O';
            case 1 -> 'X';
            default -> '.';
        };
    }

    // The grid representation
    private final int[][] grid;

    // The last player to move
    final int last;

    // The count of occupied cells
    private final int count;

    // Connect4 standard dimensions
    private final static int rows = 6;
    private final static int cols = 7;
}