package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Position;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.TicTacToePosition;
import java.util.*;

public class TicTacToeState implements State<TicTacToe> {

    /**
     * Method to yield the game of which this is a State.
     *
     * @return a G
     */
    TicTacToe game;
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
        return switch (ticTacToePosition.last) {
            case 0 -> X;
            case 1, -1 -> O;
            default -> blank;
        };
    }

    /**
     * @return the Position of this State.
     */
    public TicTacToePosition position() {
        return this.ticTacToePosition;
    }

    /**
     * Method to determine if this State represents the end of the game?
     *
     * @return an optional int if this State is a win/loss/draw.
     */
    public Optional<Integer> winner() {
        return ticTacToePosition.winner();
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

    /**
     * Get the moves that can be made directly from the given state.
     * The moves can be in any order--the order will be randomized for usage.
     *
     * @return all the possible moves from this state.
     */
    public Collection<Move<TicTacToe>> moves(int player) {
        if (player == ticTacToePosition.last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<int[]> moves = ticTacToePosition.moves(player);
  //      int step = 0;
 //       System.out.println("Size="+moves.size());
//        for (int[] move : moves) {
//            step++;
//            System.out.println("Move"+ step +" i = " + move[0] + ", j = " + move[1]);
//        }
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
        return new TicTacToeState(game,random, ticTacToePosition.move(move.player(), ints[0], ints[1]));
    }

    /**
     * Is the game over?
     *
     * @return true if position is full or if position is a winner.
     */
    public boolean isTerminal() {
        return ticTacToePosition.full() || ticTacToePosition.winner().isPresent();
    }

    @Override
    public String toString() {
        return position().render();
    }

    public TicTacToeState(TicTacToePosition ticTacToePosition) {
        this.ticTacToePosition = ticTacToePosition;
        this.game = new TicTacToe();
    }
    public TicTacToeState(){
        this(TicTacToe.startingPosition());
    }
    public TicTacToeState(TicTacToe game,Random random, TicTacToePosition ticTacToePosition) {
        this.game = game;
        this.random=random;
        this.ticTacToePosition = ticTacToePosition;
    }
//    public TicTacToeState(TicTacToeState last, Position position) {
//        this.game = last.game;
//        this.random=last.random;
//        this.position = position;
//    }
    private Random random;
    private final TicTacToePosition ticTacToePosition;
    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;
}
