package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

public class Connect4State implements State<Connect4> {


    @Override
    public Connect4 game() {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int player() {
        return 0;
    }

    @Override
    public Optional<Integer> winner() {
        return Optional.empty();
    }

    @Override
    public Random random() {
        return null;
    }

    @Override
    public Collection<Move<Connect4>> moves(int player) {
        return List.of();
    }

    @Override
    public State<Connect4> next(Move<Connect4> move) {
        return null;
    }

    @Override
    public Iterator<Move<Connect4>> moveIterator(int player) {
        return State.super.moveIterator(player);
    }

    @Override
    public Move<Connect4> chooseMove(int player) {
        return State.super.chooseMove(player);
    }
}
