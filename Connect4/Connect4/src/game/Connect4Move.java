package game;

public class Connect4Move {
    private final int player;
    private final int col;

    public Connect4Move(int player, int col) {
        this.player = player;
        this.col = col;
    }

    public int player() {
        return player;
    }

    public int move() {
        return col;
    }

    public int[] getCoordinates() {
        return new int[]{col, 0};
    }
}