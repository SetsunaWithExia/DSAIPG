package game;

public class TicTacToeMove {
    /**
     * @return the player for this Move.
     */
    public int player() {
        return player;
    }

    /**
     * Primary constructor.
     *
     * @param player the player.
     * @param i      the row.
     * @param j      the column.
     */
    public TicTacToeMove(int player, int i, int j) {
        this.player = player;
        this.i = i;
        this.j = j;
    }

    /**
     * @return this move as an array of two coordinates: row and column.
     */
    public int[] getCoordinates() {
        return new int[]{i, j};
    }
    
    private final int player;
    private final int i;
    private final int j;
}