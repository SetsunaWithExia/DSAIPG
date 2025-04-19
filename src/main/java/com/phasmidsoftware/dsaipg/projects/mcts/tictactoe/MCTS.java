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

   // public static void main(String[] args) {
        // Please use TicTacToe to run the game
    //}

    public MCTS(TicTacToeNode root,int resource) {
        this.root = root;
        root.initializeRoot();
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
//        System.out.println(cur.state());
//        System.out.println("vis = "+cur.vis());
        if(cur.isLeaf()){
            return visit(cur);
        }
        if(cur.children().isEmpty()){
            cur.explore();
        }

        if(cur.fullyExpanded()){
            //System.out.println("Finding Best Child");
           // System.out.println("Finding Best Child");

            TicTacToeNode bestChild = cur.childWithHighestUCT();
            TicTacToeState leaf = traverse(bestChild);
            backPropagate(cur, leaf);
            return leaf;
        }else {
            //System.out.println("Visiting");
            TicTacToeNode unvisitedChild = cur.unvisited();
            if (unvisitedChild != null) {
                TicTacToeState leaf = visit(unvisitedChild);
                backPropagate(cur, leaf);
                return leaf;
            } else {

                throw new RuntimeException("Unvisited child Not Found");
            }
        }
    }
    /**
        Start From an unvisited children, simulate its result randomly and return an end Node
        @return Result node of simulation
    */
    public TicTacToeState visit(TicTacToeNode cur) {

        TicTacToeState leaf = simulate(cur);
        backPropagate(cur,leaf);

        return leaf;
    }
    /**
     * Use random step method to keep going the game for both players
     * @return Result of random playing for both players
     */


    public TicTacToeState simulate(TicTacToeNode cur) {
        //System.out.println("Simulating " + cur.state());
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