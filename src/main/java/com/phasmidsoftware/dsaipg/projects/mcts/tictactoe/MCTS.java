/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS<G extends Game> implements com.phasmidsoftware.dsaipg.projects.mcts.core.MCTS {

   // public static void main(String[] args) {
        // Please use TicTacToe to run the game
    //}

    public MCTS(TicTacToeNode root,int resource) {
        this.root = root;
        root.initializeRoot();
        this.resource = resource;
    }
    @Override
    public TicTacToeMove getBestMove() {
        int remain = resource;
        while(remain > 0) {
            traverse(root);
            remain--;
        }

        TicTacToeState childState =   root.childWithHighestUCT().state();
        int[] nextStep = TicTacToePosition.Getmove(root.state().position(), childState.position());
        return new TicTacToeMove(root.state().player(),nextStep[0],nextStep[1]);
    }
    @Override
    public State simulate(Node cur) {
        TicTacToeState state = new TicTacToeState((TicTacToe) cur.state().game(), cur.state().random(), (TicTacToePosition) cur.state().position());
        int player = cur.state().player();
        while(!state.isTerminal()){
            state=state.next(state.chooseMove(player));
            player ^= 1;
        }
        return state;
    }

    private final TicTacToeNode root;
    private final int resource;
}