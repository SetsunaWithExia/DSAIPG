package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
public class TicTacToeMoveTest {
    @Test
    public void testTicTacToeMove() {
        TicTacToeMove move = new TicTacToeMove(1,2,0);
        int[] pos = {2,0};
        assertEquals(pos[0],move.getCoordinates()[0]);
        assertEquals(pos[1],move.getCoordinates()[1]);
    }
    @Test
    public void testTicTacToeMovePlayer() {
        TicTacToeMove move = new TicTacToeMove(1,2,0);
        assertEquals(1,move.player());
    }

}
