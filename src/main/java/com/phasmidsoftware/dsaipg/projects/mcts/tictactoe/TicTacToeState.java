package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

import static com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.TicTacToe.startingPosition;

public class TicTacToeState implements State<TicTacToe> {

    /**
     * Method to yield the game of which this is a State.
     *
     * @return a G
     */
    TicTacToe game;
    public TicTacToeState(TicTacToe game, Position position) {
        this.game = game;
        this.position = position;
    }
    public TicTacToeState(Position position,int player) {
        this.position = position;
        this.player = player;
    }
    public TicTacToeState() {
        this(startingPosition());
    }


    public TicTacToe game() {
        return this.game;
    }

    /**
     * Method to determine the player who plays to this State.
     * The first player to play is considered to be "white" by analogy with chess.
     *
     * @return a non-negative integer.
     */

    public int player() {
        return switch (position.last) {
            case 0, -1 -> X;
            case 1 -> O;
            default -> blank;
        };
    }

    /**
     * @return the Position of this State.
     */
    public Position position() {
        return this.position;
    }

    /**
     * Method to determine if this State represents the end of the game?
     *
     * @return an optional int if this State is a win/loss/draw.
     */
    public Optional<Integer> winner() {
        return position.winner();
    }

    /**
     * A random source associated with this State.
     * Currently, it is set to the same random as used by TicTacToe.
     * If you need a different random for each state, override this.
     *
     * @return the appropriate RandomState.
     */
    public Random random() {
        return random;
    }
    public TicTacToeState(Random random, Position position) {
        this.random = random;
        this.position = position;
    }
    /**
     * Get the moves that can be made directly from the given state.
     * The moves can be in any order--the order will be randomized for usage.
     *
     * @return all the possible moves from this state.
     */
    public Collection<Move<TicTacToe>> moves(int player) {
        if (player == position.last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<int[]> moves = position.moves(player);
        ArrayList<Move<TicTacToe>> list = new ArrayList<>();
        for (int[] coordinates : moves) list.add(new TicTacToeMove(player, coordinates[0], coordinates[1]));
        return list;
    }

    /**
     * Implement the given move on the given state.
     *
     * @param move the move to implement.
     * @return a new state.
     */
    public TicTacToeState next(Move<TicTacToe> move) {
        TicTacToeMove ticTacToeMove = (TicTacToeMove) move;
        int[] ints = ticTacToeMove.move();
        return new TicTacToeState(position.move(move.player(), ints[0], ints[1]));
    }

    /**
     * Is the game over?
     *
     * @return true if position is full or if position is a winner.
     */
    public boolean isTerminal() {
        return position.full() || position.winner().isPresent();
    }
    public boolean isFull(){
        return position.full();
    }
    @Override
    public String toString() {
        return "TicTacToe{\n" +
                position +
                "\n}";
    }
    public String printState(){
        return position.render();
    }
    public TicTacToeState(Position position) {
        this.position = position;
    }

    private Random random;
    private final Position position;
    public  int player;
    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;
}
