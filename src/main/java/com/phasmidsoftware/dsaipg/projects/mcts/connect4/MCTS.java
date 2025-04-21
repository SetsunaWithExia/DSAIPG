/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.connect4;


import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

/**
 * Class to represent a Monte Carlo Tree Search for Connect4.
 */
public class MCTS implements com.phasmidsoftware.dsaipg.projects.mcts.core.MCTS {

    public MCTS (Connect4Node root,int resource) {
        this.root = root;
        root.initializeRoot();
        this.resource = resource;
    }
    public Connect4Move getBestMove() {
        int remain = resource;
        while(remain > 0) {
            traverse(root);
            remain--;
        }

        Connect4State childState =   root.childWithHighestUCT().state();
        int nextStep = Connect4Position.Getmove(root.state().position(), childState.position());
        return new Connect4Move(root.state().player(),nextStep);
    }

    /**
     Start From an unvisited children, simulate its result randomly and return an end Node
     @return Result node of simulation
     */

    /**
     * Use random step method to keep going the game for both players
     * @return Result of random playing for both players
     */

    public State simulate(Node cur) {

        Connect4State state = new Connect4State((Connect4) cur.state().game(), cur.state().random(), (Connect4Position) cur.state().position());
        int player = cur.state().player();
        while(!state.isTerminal()){
            state=state.next(state.chooseMove(player));
            player ^= 1;
        }
        return state;
    }

    private final Connect4Node root;
    private final int resource;
}