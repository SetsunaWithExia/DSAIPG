package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.Assert.*;

public class TicTacToeTest {

    /**
     *
     */
    @Test
    public void runGame() {
        long seed = 0L;
        TicTacToe target = new TicTacToe(seed); // games run here will all be deterministic.
        State<TicTacToe> state = target.runGame();
        Optional<Integer> winner = state.winner();
        if (winner.isPresent()) assertEquals(Integer.valueOf(TicTacToe.O), winner.get());
        else fail("no winner");
    }
    @Test
    public void runGameMCTST1() {
        long seed = 100L;
        TicTacToe target = new TicTacToe(seed);

        String inputdata = "0 1 0 2 2 2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputdata.getBytes());
        System.setIn(testIn);
        State<TicTacToe> state = target.runGameMCTS();
        Optional<Integer> winner = state.winner();
        winner.ifPresent(integer -> assertEquals(Integer.valueOf(TicTacToe.O), integer));
    }
    @Test
    public void runGameMCTST2() {
        long seed = 200L;
        TicTacToe target = new TicTacToe(seed);
        String inputdata = "2 0 0 1 1 2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputdata.getBytes());
        System.setIn(testIn);
        State<TicTacToe> state = target.runGameMCTS();
        Optional<Integer> winner = state.winner();
        winner.ifPresent(integer -> assertEquals(Integer.valueOf(TicTacToe.O), integer));
    }
    @Test
    public void runGameMCTSTDraw() {
        long seed = 1000L;
        TicTacToe target = new TicTacToe(seed);

        String inputdata = "1 1 0 2 1 0 2 1";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputdata.getBytes());
        System.setIn(testIn);
        State<TicTacToe> state = target.runGameMCTS();
        Optional<Integer> winner = state.winner();
        assertEquals(false,winner.isPresent());
    }
    @Test
    public void runGameMCTSOverlapException() {
        long seed = 1000L;
        TicTacToe target = new TicTacToe(seed);
        String inputdata = "1 1 0 2 0 0";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputdata.getBytes());
        System.setIn(testIn);
        boolean throwException = false;
        try{
            State<TicTacToe> state = target.runGameMCTS();
            Optional<Integer> winner = state.winner();
            assertEquals(false,winner.isPresent());
        }catch(Exception e){
            throwException = true;
        }
        assertTrue(throwException);
    }
    @Test
    public void runGameMCTST3() {
        long seed = 500L;
        TicTacToe target = new TicTacToe(seed);

        String inputdata = "0 2 2 0 1 2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputdata.getBytes());
        System.setIn(testIn);
        State<TicTacToe> state = target.runGameMCTS();
        Optional<Integer> winner = state.winner();
        winner.ifPresent(integer -> assertEquals(Integer.valueOf(TicTacToe.O), integer));
    }
}