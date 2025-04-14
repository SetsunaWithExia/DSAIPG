/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

    /**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }
    public boolean winner() {
        return state.winner().isPresent();
    }
    /**
     * @return the State of the Game G that this Node represents.
     */
    public TicTacToeState state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that X goes first so is "white."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<Node<TicTacToe>> children() {
        return children;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public void addChild(State<TicTacToe> state) {
        children.add(new TicTacToeNode((TicTacToeState) state));
    }
    public int unvisited(){
        for(int i = 0; i <children.size();i++ ){
            if(children.get(i).vis()==0)return i;
        }
        return -1;
    }
    public TicTacToeNode getChild(int i){
        return (TicTacToeNode) children.get(i);
    }
    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate(int childModify) {
        vis ++;
        modify = -childModify;
        val += modify;
    }

    /**
     * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
     */
    public int val() {
        return val;
    }

    /**
     * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
     */
    public int vis() {
        return vis;
    }


    @Override
    public int modify() {
        return modify;
    }

    public TicTacToeNode(TicTacToeState state) {
        this.state = state;
        if(isLeaf()){
            this.isleaf = true;
        }
        children = new ArrayList<>();
        initializeNodeData();
    }
    public boolean fullyExpanded(){
        return children.size()<vis;
    }
    public TicTacToeNode childWithHighestUCT(){
        TicTacToeNode ret=null;
        double highestUCT = Double.NEGATIVE_INFINITY;
        for(Node<TicTacToe> child : children){
            double uct = (double)child.val() / child.vis() + c * Math.sqrt(Math.log(vis)/child.vis());
            if(uct>highestUCT){
                highestUCT = uct;
                ret = (TicTacToeNode)child;
            }
        }
        return ret;
    }

    private void initializeNodeData() {
        vis = val = modify = 0;

    }
    private final TicTacToeState state;
    private final ArrayList<Node<TicTacToe>> children;
    boolean isleaf;
    private int val;
    private int vis;
    private final double c = 1.42;
    /*
    @param modify return the modification value of current node (win +1 lose -1 even 0) for backpropagation using
   */
    private int modify;
}