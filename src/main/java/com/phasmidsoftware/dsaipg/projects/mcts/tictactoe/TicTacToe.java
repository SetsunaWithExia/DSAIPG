/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

/**
 * Class which models the game of TicTacToe.
 */
public class TicTacToe implements Game<TicTacToe> {
    /**
     * Main program to run a random TicTacToe game.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        // NOTE the behavior of the game to be run will be based on the TicTacToe instance field: random.
        State<TicTacToe> state = new TicTacToe(1000).runGameMCTS();
        if (state.winner().isPresent()) {
            System.out.println("!!!!!!!!!!!! Three In A Row !!!!!!!!!!!!");
            System.out.println("TicTacToe winner is: " + (state.winner().get() == 0 ? "Machine":"Human"));
        }

        else System.out.println("TicTacToe: draw");
//        String start = ". . .\n. . .\n. . .";
//
//        Position position = Position.parsePosition(start,1);
//        System.out.println(position.render());

//        State<TicTacToe> state1 = new TicTacToe(1000).start();
//        System.out.println(state1.toString());
    }

    public static final int X = 1;    // Human
    public static final int O = 0;   //Machine
    public static final int blank = -1;

    /**
     * Method to yield a starting position.
     *
     * @return a Position.
     */
    static TicTacToePosition startingPosition() {
        return TicTacToePosition.parsePosition(". . .\n. . .\n. . .", blank);
    }

    /**
     * Run a TicTacToe game.
     *
     * @return the terminal State.
     */
    TicTacToeState runGame() {
        TicTacToeState state = start();
        int player = opener();

        while (!state.isTerminal()) {
            state = state.next(state.chooseMove(player));
            player = 1 - player;
        }
        return state;
    }
    TicTacToeState runGameMCTS(){
        TicTacToeState state = start();
        Scanner scanner = new Scanner(System.in);
        while (!state.isTerminal()) {
            if(state.player() == opener()){         //if player is opener (machine) then use MCTS move
                System.out.println("Game Start");
                System.out.println("Machine Round");
                //System.out.println("MCTS Searching...");
                MCTS mcts = new MCTS(new TicTacToeNode(state),362880);//362880

               // System.out.println("MCTS Moving...");
                TicTacToeMove move = mcts.getBestMove();
                int[] cordinates = move.getCoordinates();
                System.out.printf("MCTS choose row: %d , column %d\n",cordinates[0],cordinates[1]);
                state = state.next(move);
                System.out.println(state);
            }else{                              //player is human, waiting for human input
                System.out.println("Human Round");
                int i,j;
                System.out.println("Please enter row and column");
                i = scanner.nextInt();
                j = scanner.nextInt();
                TicTacToeMove move = new TicTacToeMove(state.player(), i,j);
                state = state.next(move);
                System.out.println(state);
            }
        }
        return state;
    }
    /**
     * This method determines the opening player (the "white" by analogy with chess).
     * NOTE this should agree with
     *
     * @return the opening player.
     */
    public int opener() {
        return O;
    }
    public int opener(int player) {
        return player;
    }
    /**
     * Get the starting state for this game.
     *
     * @return a State of TicTacToe.
     */
    public TicTacToeState start() {
        return new TicTacToeState(this,random,TicTacToe.startingPosition());
    }

    /**
     * Primary constructor.
     *
     * @param random a random source.
     */
    public TicTacToe(Random random) {
        this.random = random;
    }

    /**
     * Secondary constructor.
     *
     * @param seed a seed for the random source.
     */
    public TicTacToe(long seed) {
        this(new Random(seed));
    }

    /**
     * Secondary constructor which uses the current time as seed.
     */
    public TicTacToe() {
        this(System.currentTimeMillis());
    }

    private final Random random;


}