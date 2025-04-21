package com.phasmidsoftware.dsaipg.projects.mcts.Connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.connect4.Connect4;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.TicTacToe;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class Connect4Test {
    @Test
    public void runGameMCTS(){
        String inputdata = "2 3 4 4 5 3 6 3 5 4 1 0 1 2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputdata.getBytes());
        System.setIn(testIn);
        State<Connect4> state = new Connect4(1000).runGameMCTS();
        Optional<Integer> winner = state.winner();
        winner.ifPresent(integer -> assertEquals(Integer.valueOf(TicTacToe.O), integer));
    }
}
