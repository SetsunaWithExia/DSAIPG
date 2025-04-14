/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import scala.reflect.internal.Trees;

import javax.swing.*;

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

    public MCTS(TicTacToeNode root,int resource) {
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
        return new TicTacToeMove(root.state().player,nextStep[0],nextStep[1]);
    }
    public TicTacToeNode traverse(TicTacToeNode cur) {
        if(cur.isLeaf()){
            cur.backPropagate(-1);
            return cur;
        }
        if(cur.children().isEmpty()){
            cur.explore();
        }
        if(cur.fullyExpanded()){
            TicTacToeNode bestChild = cur.childWithHighestUCT();
            backPropagate(bestChild,  traverse(bestChild));
            return cur;
        }else{
            int unvisitedChild = cur.unvisited();
            if(unvisitedChild>=0){
                backPropagate(cur,expand(cur,unvisitedChild));
            }else{
                throw new RuntimeException("Unvisited child Not Found");
            }
            return cur;
        }

    }
    public TicTacToeNode expand(TicTacToeNode cur, int child) {
        if(cur.getChild(child).isLeaf()){
            cur.getChild(child).backPropagate(cur.getChild(child).winner()?-1:0);
        }else{
            TicTacToeNode leaf = simulate(cur.getChild(child));
            cur.getChild(child).backPropagate(leaf.winner()?1:0);
        }
        return cur.getChild(child);
    }
    public TicTacToeNode simulate(TicTacToeNode cur) {
        TicTacToeState state = new TicTacToeState(cur.state().random(),cur.state().position());
        int player = cur.state().player();
        while(!state.isTerminal()){
            state=state.next(state.chooseMove(player));
            player ^= 1;
        }
        return new TicTacToeNode(state);
    }
    public void backPropagate(TicTacToeNode parent, TicTacToeNode child) {
        parent.backPropagate(child.modify());
    }

    private final TicTacToeNode root;
    private final int resource;
}