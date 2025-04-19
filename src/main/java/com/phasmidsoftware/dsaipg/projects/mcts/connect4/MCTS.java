/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.*;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {

        TicTacToe ticTacToe = new TicTacToe(100);
        MCTS mcts = new MCTS(new TicTacToeNode(ticTacToe.start()),100);
        Node<TicTacToe> root = mcts.root;

        // This is where you process the MCTS to try to win the game.
    }

    public MCTS(TicTacToeNode root, int resource) {
        this.root = root;
        this.resource = resource;
    }
    public TicTacToeMove getBestMove() {
        int remain = resource;
        while(remain > 0) {
            traverse(root);
            remain--;
        }

        TicTacToeState childState =   root.childWithHighestUCT().state();
        int[] nextStep = Position.Getmove(root.state().position(), childState.position());
        return new TicTacToeMove(root.state().player(),nextStep[0],nextStep[1]);
    }
    public TicTacToeState traverse(TicTacToeNode cur) {
        if(cur.isLeaf()) return expand(cur);

        if(cur.children().isEmpty()) cur.explore();

        if(cur.fullyExpanded()){
            TicTacToeNode bestChild = cur.childWithHighestUCT();
            TicTacToeState leaf = traverse(bestChild);
            backPropagate(cur,  leaf);
            return leaf;
        }else{
            TicTacToeState leaf = expand(cur.unvisited());
            backPropagate(cur, leaf);
            return leaf;
        }
    }
    public TicTacToeState expand(TicTacToeNode cur) {

            TicTacToeState leaf = simulate(cur);
            backPropagate(cur,leaf);

        return leaf;
    }
    public TicTacToeState simulate(TicTacToeNode cur) {
        TicTacToeState state = new TicTacToeState(cur.state().game(), cur.state().random(),cur.state().position());
        int player = cur.state().player();
        while(!state.isTerminal()){
            state=state.next(state.chooseMove(player));
            player ^= 1;
        }
        return state;
    }
    public void backPropagate(TicTacToeNode ancestorNode, TicTacToeState leafState) {
        int leafWin=leafState.winner().isPresent() ? 1:0;
        if(ancestorNode.state().player()!=leafState.player()){
            ancestorNode.setUpdateValue(-leafWin);
        }else{
            ancestorNode.setUpdateValue(leafWin);
        }
    }

    private final TicTacToeNode root;
    private final int resource;
}