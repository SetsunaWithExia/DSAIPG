/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.connect4;


/**
 * Class to represent a Monte Carlo Tree Search for Connect4.
 */
public class MCTS {

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
    public Connect4State traverse(Connect4Node cur) {
        if(cur.isLeaf()){
            return visit(cur);
        }
        if(cur.children().isEmpty()){
            cur.explore();
        }

        if(cur.fullyExpanded()){

            Connect4Node bestChild = cur.childWithHighestUCT();
            Connect4State leaf = traverse(bestChild);
            backPropagate(cur, leaf);
            return leaf;
        }else {

            Connect4Node unvisitedChild = cur.unvisited();
            if (unvisitedChild != null) {
                Connect4State leaf = visit(unvisitedChild);
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
    public Connect4State visit(Connect4Node cur) {

        Connect4State leaf = simulate(cur);
        backPropagate(cur,leaf);

        return leaf;
    }
    /**
     * Use random step method to keep going the game for both players
     * @return Result of random playing for both players
     */


    public Connect4State simulate(Connect4Node cur) {

        Connect4State state = new Connect4State(cur.state().game(), cur.state().random(),cur.state().position());
        int player = cur.state().player();
        while(!state.isTerminal()){
            state=state.next(state.chooseMove(player));
            player ^= 1;
        }
        return state;
    }
    public void backPropagate(Connect4Node ancestorNode, Connect4State leafState) {
        int leafWin=leafState.winner().isPresent() ? 1:0;
        if(ancestorNode.state().player()!=leafState.player()){
            ancestorNode.setUpdateValue(-leafWin);
        }else{
            ancestorNode.setUpdateValue(leafWin);
        }
    }

    private final Connect4Node root;
    private final int resource;
}