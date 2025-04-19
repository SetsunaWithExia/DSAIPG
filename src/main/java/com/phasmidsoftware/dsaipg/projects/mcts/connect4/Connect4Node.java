package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.Collection;
import java.util.List;

public class Connect4Node implements Node<Connect4> {
    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public State<Connect4> state() {
        return null;
    }

    @Override
    public boolean black() {
        return false;
    }

    @Override
    public Collection<Node<Connect4>> children() {
        return List.of();
    }

    @Override
    public void explore() {
        Node.super.explore();
    }

    @Override
    public void addChild(State<Connect4> state) {

    }

    @Override
    public int val() {
        return 0;
    }

    @Override
    public int vis() {
        return 0;
    }


}
