package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.*;

import java.util.Random;
import java.util.Scanner;

public class Connect4 implements Game <Connect4>{
    public static void  main(String[] args) {
        State<Connect4> state = new Connect4(1000).runGameMCTS();
        if (state.winner().isPresent()){
            System.out.println("!!!!!!!!!!!! Connect 4 !!!!!!!!!!!!");
            System.out.println("Connect4 winner is: " + (state.winner().get() == 0 ? "Machine":"Human"));
        }
        else System.out.println("Connect4: draw");
    }
    public Connect4(Random random) {
        this.random = random;
    }
    public Connect4(long seed) {
        this(new Random(seed));
    }
    @Override
    public Connect4State start() {
        return new Connect4State(this,random,Connect4.startingPosition());
    }
    @Override
    public int opener() {
        return O;
    }


    /**
     * Method to yield a starting position.
     *
     * @return a Position.
     */
    static Connect4Position startingPosition() {
        return Connect4Position.parsePosition(". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n", blank);
    }

    public Connect4State runGameMCTS(){
        Connect4State state = (Connect4State) start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("------Game Start------");
        System.out.println(state+"\n");

        while (!state.isTerminal()) {
            if(state.player() == opener()){             //if player is opener (machine) then use MCTS move
                System.out.println("------Machine Round------");
//                System.out.println("MCTS Searching...");
                MCTS mcts = new MCTS(new Connect4Node(state),1000);
 //               System.out.println("MCTS Moving...");
                Connect4Move move = mcts.getBestMove();
                int[] coordinates = move.getCoordinates();
                System.out.printf("MCTS choose column  %d \n",coordinates[0]);
                state = state.next(move);
                System.out.println(state+"\n");
            }else{                              //player is human, waiting for human input
                System.out.println("------Human Round------");
                int column;
                System.out.println("Please enter a column number");

                column = scanner.nextInt();
                Connect4Move move = new Connect4Move(state.player(), column);
                state = state.next(move);
                System.out.println(state+"\n");
            }
        }
        return state;
    }
    private final Random random;
    public static final int X = 1;    // Human
    public static final int O = 0;   //Machine
    public static final int blank = -1;

}
