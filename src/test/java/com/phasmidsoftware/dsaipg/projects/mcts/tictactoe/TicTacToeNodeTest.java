package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicTacToeNodeTest {

    @Test
    public void winsAndPlayouts() {//*
        TicTacToeState state = new TicTacToeState(TicTacToePosition.parsePosition("X . 0\nX O .\nX . 0", TicTacToe.X));
        TicTacToeNode node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node,2);
        mcts.traverse(node);
        mcts.traverse(node);
        assertTrue(node.isLeaf());
        assertEquals(2, node.val());
        assertEquals(3, node.vis());
    }

    @Test
    public void state() {
        TicTacToeState state = new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertEquals(state, node.state());
    }

    @Test
    public void black() {
        TicTacToeState state = new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.black());
    }
}