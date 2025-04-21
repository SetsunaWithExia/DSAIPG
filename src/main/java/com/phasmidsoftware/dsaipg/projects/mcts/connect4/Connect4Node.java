/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import java.util.ArrayList;
import java.util.Collection;


public class Connect4Node implements Node<Connect4> {

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
    public Connect4State state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that O goes first so is "black."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean black() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<Node<Connect4>> children() {
        return children;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public void addChild(State<Connect4> state) {
        children.add(new Connect4Node((Connect4State) state));
    }
    public Connect4Node unvisited(){
        for (Node<Connect4> child : children) {
            if (child.vis() == 0) return (Connect4Node) child;
        }
        return null;
    }
    public Connect4Node getChild(int i){
        return (Connect4Node) children.get(i);
    }
    /**
     * This method update the number of wins and playouts according to the children states.
     */
    public void setUpdateValue(int updateValue) {
        vis ++;
        val += updateValue;
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

    public Connect4Node(Connect4State state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData();
    }

    public boolean fullyExpanded(){
        return children.size()<vis;
    }
    public Connect4Node childWithHighestUCT(){
        Connect4Node ret=null;

        double highestUCT = Double.NEGATIVE_INFINITY;
        for(Node<Connect4> child : children){
            double uct = (double)child.val() / child.vis() + C * Math.sqrt(Math.log(vis)/child.vis());
            if(uct>highestUCT){
                highestUCT = uct;
                ret = (Connect4Node)child;
            }
        }
        if(ret!=null) return ret;
        System.out.println("children:");
        for(Node<Connect4> child : children){
            System.out.println("Child vis:"+child.vis()+"Child val:"+child.val());
            System.out.println(child.state());
        }
        return null;
    }

    private void initializeNodeData() {
        vis = val = 0;
    }
    public void initializeRoot(){
        vis = 1;
    }
    private final Connect4State state;
    private final ArrayList<Node<Connect4>> children;
    private int val;
    private int vis;
    /*
    @param modify return the modification value of current node (win +1 lose -1 even 0) for backpropagation using
   */
}