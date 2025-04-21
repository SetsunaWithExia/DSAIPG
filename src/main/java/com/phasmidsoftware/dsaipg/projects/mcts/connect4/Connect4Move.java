package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;

public class Connect4Move implements Move{


    @Override
    public int player() {
        return player;
    }

    /**
     * Primary constructor.
     *
     * @param player the player.
     * @param col      the column.
     */
    public Connect4Move(int player, int col) {
        this.player = player;
        this.col = col;
    }

    /**
     * @return this move as an array of two coordinates: row and column.
     */
    public int move() {
        return col;
    }
    public int[] getCoordinates() {
        return new int[]{col, 0};
    }
    private final int player;
    private final int col;

}
