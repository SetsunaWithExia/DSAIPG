package game;

import java.util.Random;

public class Connect4 {
    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;
    private final Random random;

    public Connect4(Random random) {
        this.random = random;
    }

    public Connect4(long seed) {
        this(new Random(seed));
    }

    public Connect4State start() {
        return new Connect4State(this, random, Connect4.startingPosition());
    }

    public int opener() {
        return O;
    }

    static Connect4Position startingPosition() {
        return Connect4Position.parsePosition(". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n" +
                ". . . . . . .\n", blank);
    }
}
