package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class TicTacToeStateTest {

    @Test
    public void isTerminalX() {
        String position = "X O .\nX . .\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,1));
        assertTrue(state.isTerminal());
    }
    @Test
    public void isTerminalO() {
        String position = "X O .\nX . O\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,0));
        assertFalse(state.isTerminal());
    }
    @Test
    public void isTerminalO2() {
        String position = "X O X\nO X O\nX O O";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,0));
        assertTrue(state.isTerminal());
    }
    @Test
    public void isTerminalNonPlayer() {
        String position = "X O X\nO X O\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,-1));
        assertFalse(state.isTerminal());
    }
    @Test
    public void isTerminalO3() {
        String position = "X O X\nO X O\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,0));
        assertFalse(state.isTerminal());
    }
    @Test
    public void isTerminalX1() {
        String position = "X O X\nO X O\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,1));
        assertTrue(state.isTerminal());
    }
    @Test
    public void next(){
        String position = "X O .\nX . .\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,1));
        TicTacToeMove move = new TicTacToeMove(state.player(),1 ,1);
        state = state.next(move);
        String standardRes = "X O .\nX O .\nX O .";
        assertEquals(standardRes,state.toString());
    }
    @Test
    public void Moves() {
        TicTacToeMove move = new TicTacToeMove(1,2,2);
        String position = "X O O\nX X O\nX O .";
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition(position,1));

        Collection<Move<TicTacToe>> mo = state.moves(state.player());
        assertEquals(1,mo.size());
        Move<TicTacToe> move1 = mo.iterator().next();
        int[] nextMove = move1.getCoordinates();
        int currentPlayer = move1.player();
        assertEquals(move.getCoordinates()[0],nextMove[0]);
        assertEquals(move.getCoordinates()[1],nextMove[1]);
        assertEquals(0,currentPlayer);
    }
}
