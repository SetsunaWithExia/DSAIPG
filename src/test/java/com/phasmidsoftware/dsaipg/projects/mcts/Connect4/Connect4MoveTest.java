package com.phasmidsoftware.dsaipg.projects.mcts.Connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.connect4.Connect4Move;
import org.junit.Test;
import static org.junit.Assert.*;
public class Connect4MoveTest {
    @Test
    public void move() {
        Connect4Move move = new Connect4Move(1,2);
        assertEquals(1, move.player());
        assertEquals(2, move.move());
    }
    @Test
    public void getCoordinates() {
        Connect4Move move = new Connect4Move(1,2);
        assertEquals(2, move.getCoordinates()[0]);
    }
}
