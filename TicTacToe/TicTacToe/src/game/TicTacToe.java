package game;

import java.util.Random;

/**
 * Class which models the game of TicTacToe.
 */
public class TicTacToe {

    public static final int X = 1;    // Human
    public static final int O = 0;    // Machine
    public static final int blank = -1;

    /**
     * Main program to run a random TicTacToe game.
     *
     * @param args command-line arguments.
     */
    static Position startingPosition() {
        return Position.parsePosition(". . .\n. . .\n. . .", blank);
    }

    /**
     * Method decide who start the game.
     *
     * @return an integer.
     */
    public int opener() {
        return O;
    }

    /**
     * Run a TicTacToe game.
     *
     * @return the terminal State.
     */
    public TicTacToeState start() {
        return new TicTacToeState(this, random, TicTacToe.startingPosition());
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