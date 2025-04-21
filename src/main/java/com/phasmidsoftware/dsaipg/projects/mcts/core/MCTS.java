package com.phasmidsoftware.dsaipg.projects.mcts.core;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;


import com.phasmidsoftware.dsaipg.projects.mcts.core.Position;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;


public interface MCTS <G extends Game>{
    Move<G> getBestMove();
    default State<G> traverse(Node<G> cur) {
        if(cur.isLeaf()) return visit(cur);
        if(cur.children().isEmpty()) cur.explore();

        if(cur.fullyExpanded()){
            Node<G> bestChild = cur.childWithHighestUCT();
            State<G> leaf = traverse(bestChild);
            backPropagate(cur, leaf);
            return leaf;
        }else {
            Node<G> unvisitedChild = cur.unvisited();
            if (unvisitedChild != null) {
                State<G> leaf = visit(unvisitedChild);
                backPropagate(cur, leaf);
                return leaf;
            } else
                throw new RuntimeException("Unvisited child Not Found");
        }
    }
    /**
     Start From an unvisited children, simulate its result randomly and return an end Node
     @return Result node of simulation
     */
    default State<G> visit(Node<G> cur) {

        State<G> leaf = simulate(cur);
        backPropagate(cur,leaf);

        return leaf;
    }
    /**
     * Use random step method to keep going the game for both players
     * @return Result of random playing for both players
     */


    State<G> simulate(Node<G> cur);
     default void backPropagate(Node<G> ancestorNode, State<G> leafState) {
        int leafWin = leafState.winner().isPresent() ? 1:0;
        if(ancestorNode.state().player() != leafState.player())
            ancestorNode.setUpdateValue(-leafWin);
        else
            ancestorNode.setUpdateValue(leafWin);

    }
}
