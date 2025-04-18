package com.phasmidsoftware.dsaipg.projects.mcts.connect4;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.*;

import java.util.Random;
import java.util.Scanner;

public class Connect4 implements Game <Connect4>{
    public static void  main(String[] args) {
        State<Connect4> state = new Connect4(1000).runGameMCTS();
        if (state.winner().isPresent()) System.out.println("Connect4: winner is: " + state.winner().get());
        else System.out.println("Connect4: draw");
    }
    public Connect4(Random random) {
        this.random = random;
    }
    public Connect4(long seed) {
        this(new Random(seed));
    }
    @Override
    public State<Connect4> start() {
        return new Connect4State();
    }

    @Override
    public int opener() {
        return 0;
    }


    private static Object startingPosition() {
        return null;
    }

    Connect4State runGameMCTS(){
        Connect4State state = (Connect4State) start();

        int player = opener();
        Scanner scanner = new Scanner(System.in);
        while (!state.isTerminal()) {
            if(player == opener()){             //if player is opener (machine) then use MCTS move
//                System.out.println("Machine Round");
//                System.out.println("MCTS Searching...");
//                MCTS mcts = new MCTS(state,1000);
//                System.out.println("MCTS Moving...");
//                TicTacToeMove move = mcts.getBestMove();
//                int[] cordinate = move.getCoordinates();
//                System.out.printf("MCTS choose + %d %d\n",cordinate[0],cordinate[1]);
//                state = state.next(move);
            }else{                              //player is human, waiting for human input
//                System.out.println("Human Round");
//                System.out.printf(state.toString());
//                int i,j;
//                System.out.println("Please enter row");
//                i = scanner.nextInt();
//                System.out.println("Please enter column");
//                j = scanner.nextInt();
//                TicTacToeMove move = new TicTacToeMove(player,i,j);
//                state = state.next(move);
            }
        }
        return state;
    }
    private final Random random;
}
