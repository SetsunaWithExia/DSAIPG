package com.phasmidsoftware.dsaipg.projects.mcts.Connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.connect4.Connect4Position;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Connect4PositionTest {

    @Test
    public void testEmptyPosition() {
        Connect4Position c4position = Connect4Position.emptyPosition();
        assertEquals(-1, c4position.last);
        assertEquals(0, c4position.count);
        assertTrue(c4position.winner().isEmpty());
        List<Integer> c4validMoves = c4position.moves(0);
        assertEquals(7, c4validMoves.size());
        for (int i = 0; i < 7; i++) {
            assertTrue(c4validMoves.contains(i));
        }
    }

    @Test
    public void testBasicMoves() {
        Connect4Position c4position = Connect4Position.emptyPosition();
        Connect4Position c4pos1 = c4position.move(0, 3);
        assertEquals(0, c4pos1.last);
        assertEquals(1, c4pos1.count);
        Connect4Position c4pos2 = c4pos1.move(1, 4);
        assertEquals(1, c4pos2.last);
        assertEquals(2, c4pos2.count);
        String c4expected =
                ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . O X . .";
        assertEquals(c4expected, c4pos2.render().split("\n0 1 2 3 4 5 6")[0]);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidMove_SamePlayer() {
        Connect4Position c4position = Connect4Position.emptyPosition();
        c4position = c4position.move(0, 3);
        c4position.move(0, 4);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidMove_ColumnFull() {
        Connect4Position c4position = Connect4Position.emptyPosition();
        c4position = c4position.move(0, 0);
        c4position = c4position.move(1, 0);
        c4position = c4position.move(0, 0);
        c4position = c4position.move(1, 0);
        c4position = c4position.move(0, 0);
        c4position = c4position.move(1, 0);
        c4position.move(0, 0);
    }

    @Test
    public void testHorizontalWin() {
        Connect4Position c4position = Connect4Position.emptyPosition();
        c4position = c4position.move(0, 0);
        c4position = c4position.move(1, 0);
        c4position = c4position.move(0, 1);
        c4position = c4position.move(1, 1);
        c4position = c4position.move(0, 2);
        c4position = c4position.move(1, 2);
        Connect4Position c4winPos = c4position.move(0, 3);
        Optional<Integer> c4winner = c4winPos.winner();
        assertTrue(c4winner.isPresent());
        assertEquals(0, c4winner.get().intValue());
    }

    @Test
    public void testVerticalWin() {
        Connect4Position c4position = Connect4Position.emptyPosition();
        c4position = c4position.move(0, 0);
        c4position = c4position.move(1, 1);
        c4position = c4position.move(0, 2);
        c4position = c4position.move(1, 1);
        c4position = c4position.move(0, 0);
        c4position = c4position.move(1, 1);
        c4position = c4position.move(0, 2);
        Connect4Position c4winPos = c4position.move(1, 1);
        Optional<Integer> c4winner = c4winPos.winner();
        assertTrue("Player 1 should win", c4winner.isPresent());
        assertEquals("Winner should be player 1", 1, c4winner.get().intValue());
        System.out.println(c4winPos.render());
    }
}