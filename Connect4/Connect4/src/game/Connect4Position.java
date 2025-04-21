package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Connect4Position {
    public final int[][] grid;
    public final int last;
    public final int count;
    private final static int rows = 6;
    private final static int cols = 7;

    Connect4Position(int[][] grid, int count, int last) {
        this.grid = grid;
        this.count = count;
        this.last = last;
    }

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
                if(prePosition.grid[row][col] != aftPosition.grid[row][col]) return col;
        return -1;
    }

    public Connect4Position move(int player, int col) {
        if (full()) throw new RuntimeException("Position is full");
        if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        if (col < 0 || col >= cols) throw new RuntimeException("Column out of bounds: " + col);
        int[][] matrix = copyGrid();
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

    public List<Integer> moves(int player) {
        if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<Integer> result = new ArrayList<>();
        for (int j = 0; j < cols; j++) {
            if (grid[0][j] < 0) {
                result.add(j);
            }
        }
        return result;
    }

    boolean full() {
        return count == rows * cols;
    }

    private int[][] copyGrid() {
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            result[i] = Arrays.copyOf(grid[i], cols);
        return result;
    }

    public Optional<Integer> winner() {
        if (count >= 7 && fourInARow()) {
            return Optional.of(last);
        }
        return Optional.empty();
    }

    boolean fourInARow() {
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
        return false;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(render(grid[i][j]));
                if (j < cols - 1) sb.append(' ');
            }
            if (i < rows - 1) sb.append('\n');
        }
        sb.append('\n');
        for (int j = 0; j < cols; j++) {
            sb.append(j);
            if (j < cols - 1) sb.append(' ');
        }
        return sb.toString();
    }

    private char render(int x) {
        return switch (x) {
            case 0 -> 'O';
            case 1 -> 'X';
            default -> '.';
        };
    }

    public static Connect4Position emptyPosition() {
        int[][] emptyGrid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                emptyGrid[i][j] = -1;
            }
        }
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