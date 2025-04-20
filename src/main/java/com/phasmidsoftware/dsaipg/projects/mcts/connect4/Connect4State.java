package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.time.Clock;
import java.util.*;

import static com.phasmidsoftware.dsaipg.projects.mcts.connect4.Connect4.startingPosition;

public class Connect4State implements State<Connect4> {

    /**
     * Method to yield the game of which this is a State.
     *
     * @return a G
     */
    Connect4 game;
    public Connect4 game() {
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
            case 0 -> X;
            case 1, -1 -> O;
            default -> blank;
        };
    }

    /**
     * @return the Position of this State.
     */
    public Connect4Position position() {
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

    /**
     * Get the moves that can be made directly from the given state.
     * The moves can be in any order--the order will be randomized for usage.
     *
     * @return all the possible moves from this state.
     */
    public Collection<Move<Connect4>> moves(int player) {
        if (player == position.last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<Integer> moves = position.moves(player);
        //      int step = 0;
        //       System.out.println("Size="+moves.size());
//        for (int[] move : moves) {
//            step++;
//            System.out.println("Move"+ step +" i = " + move[0] + ", j = " + move[1]);
//        }
        ArrayList<Move<Connect4>> list = new ArrayList<>();
        for (int column : moves) list.add(new Connect4Move(player, column));
        return list;
    }

    /**
     * Implement the given move on the given state.
     *
     * @param move the move to implement.
     * @return a new state.
     */
    public Connect4State next(Move<Connect4> move) {
        Connect4Move connect4Move = (Connect4Move) move;
        int column = connect4Move.move();
        return new Connect4State(game,random,position.move(move.player(), column));
    }

    /**
     * Is the game over?
     *
     * @return true if position is full or if position is a winner.
     */
    public boolean isTerminal() {
        return position.full() || position.winner().isPresent();
    }

    @Override
    public String toString() {
        return position().render();
    }

    public Connect4State(Connect4Position position) {
        this.position = position;
    }
    public Connect4State(){
        this(Connect4.startingPosition());
    }
    public Connect4State(Connect4 game,Random random, Connect4Position position) {
        this.game = game;
        this.random=random;
        this.position = position;
    }
    //    public TicTacToeState(TicTacToeState last, Position position) {
//        this.game = last.game;
//        this.random=last.random;
//        this.position = position;
//    }
    private Random random;
    private final Connect4Position position;
    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;
}
