package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TicTacToePositionTest {

    @Test(expected = RuntimeException.class)
    public void testMove_2() {
        String grid = "X . .\n. O .\n. . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        target.move(1, 0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testMove_1() {
        String grid = "X X 0\nX O 0\nX X 0";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        target.move(1, 0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testMove0() {
        String grid = "X . .\n. O .\n. . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        target.move(1, 0, 1);
    }

    @Test
    public void testMove1() {
        String grid = "X . .\n. O .\n. . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        TicTacToePosition moved = target.move(0, 0, 1);
        TicTacToePosition expected = TicTacToePosition.parsePosition(grid.replaceFirst("\\.", "O"), 0);
        assertEquals(expected, moved);
    }

    @Test
    public void testMoves() {
        String grid = "X . .\n. O .\n. . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        List<int[]> moves = target.moves(0);
        assertEquals(6, moves.size());
        assertArrayEquals(new int[]{0, 1}, moves.get(0));
        assertArrayEquals(new int[]{0, 2}, moves.get(1));
        assertArrayEquals(new int[]{1, 0}, moves.get(2));
        assertArrayEquals(new int[]{1, 2}, moves.get(3));
        assertArrayEquals(new int[]{2, 0}, moves.get(4));
        assertArrayEquals(new int[]{2, 1}, moves.get(5));
    }

    @Test
    public void testReflect() {
    }

    @Test
    public void testRotate() {
    }

    @Test
    public void testWinner0() {
        String grid = "X . .\n. O .\n. . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        assertTrue(target.winner().isEmpty());
    }

    @Test
    public void testWinner1() {
        String grid = "X . 0\nX O .\nX . 0";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        Optional<Integer> winner = target.winner();
        assertTrue(winner.isPresent());
        assertEquals(Integer.valueOf(1), winner.get());
    }

    @Test
    public void testWinner2() {
        String grid = "0 . X\n0 X .\nO . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 0);
        Optional<Integer> winner = target.winner();
        assertTrue(winner.isPresent());
        assertEquals(Integer.valueOf(0), winner.get());
    }

    @Test
    public void testProjectRow() {
        String grid = "X . 0\nX O .\nX . 0";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        assertArrayEquals(new int[]{1, -1, 0}, target.projectRow(0));
        assertArrayEquals(new int[]{1, 0, -1}, target.projectRow(1));
        assertArrayEquals(new int[]{1, -1, 0}, target.projectRow(2));
    }

    @Test
    public void testProjectCol() {
        String grid = "X . 0\nX O .\nX . 0";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        assertArrayEquals(new int[]{1, 1, 1}, target.projectCol(0));
        assertArrayEquals(new int[]{-1, 0, -1}, target.projectCol(1));
        assertArrayEquals(new int[]{0, -1, 0}, target.projectCol(2));
    }

    @Test
    public void testProjectDiag() {
        String grid = "X . 0\nX O .\nX . 0";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        assertArrayEquals(new int[]{1, 0, 0}, target.projectDiag(true));
        assertArrayEquals(new int[]{1, 0, 0}, target.projectDiag(false));
    }

    @Test
    public void testParseCell() {
        assertEquals(0, TicTacToePosition.parseCell("0"));
        assertEquals(0, TicTacToePosition.parseCell("O"));
        assertEquals(0, TicTacToePosition.parseCell("o"));
        assertEquals(1, TicTacToePosition.parseCell("X"));
        assertEquals(1, TicTacToePosition.parseCell("x"));
        assertEquals(1, TicTacToePosition.parseCell("1"));
        assertEquals(-1, TicTacToePosition.parseCell("."));
        assertEquals(-1, TicTacToePosition.parseCell("a"));
    }

    @Test
    public void testThreeInARow() {
        String grid = "X . 0\nX O .\nX . 0";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        assertTrue(target.threeInARow());
    }

    @Test
    public void testFull() {
        assertFalse(TicTacToePosition.parsePosition("X . 0\nX O .\nX . 0", 1).full());
        assertTrue(TicTacToePosition.parsePosition("X X 0\nX O 0\nX X 0", 1).full());
    }

    @Test
    public void testRender() {
        String grid = "X . .\n. O .\n. . X";
        TicTacToePosition target = TicTacToePosition.parsePosition(grid, 1);
        assertEquals(grid, target.render());
    }

    @Test
    public void testToString() {
        TicTacToePosition target = TicTacToePosition.parsePosition("X . .\n. O .\n. . X", 1);
        assertEquals("1,-1,-1\n-1,0,-1\n-1,-1,1", target.toString());
    }
}