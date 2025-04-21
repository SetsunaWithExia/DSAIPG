package game;

import java.util.*;

public class TicTacToeState {

    /**
     * Get Game of Current State
     *
     * @return TicTacToe Game
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
        return switch (position.last) {
            case 0 -> X;
            case 1, -1 -> O;
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

    /**
     * Get the moves that can be made directly from the given state.
     * The moves can be in any order--the order will be randomized for usage.
     *
     * @return all the possible moves from this state.
     */
    public Collection<TicTacToeMove> moves(int player) {
        if (player == position.last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<int[]> moves = position.moves(player);
        ArrayList<TicTacToeMove> list = new ArrayList<>();
        for (int[] coordinates : moves) list.add(new TicTacToeMove(player, coordinates[0], coordinates[1]));
        return list;
    }

    /**
     * Implement the given move on the given state.
     *
     * @param move the move to implement.
     * @return a new state.
     */
    public TicTacToeState next(TicTacToeMove move) {
        int[] ints = move.getCoordinates();
        return new TicTacToeState(game, random, position.move(move.player(), ints[0], ints[1]));
    }

    /**
     * Is the game over?
     *
     * @return true if position is full or if position is a winner.
     */
    public boolean isTerminal() {
        return position.full() || position.winner().isPresent();
    }

    /**
     * 
     * We chose a movement randomly
     */
    public TicTacToeMove chooseMove(int player) {
        Collection<TicTacToeMove> moves = moves(player);
        if (moves.isEmpty()) 
            throw new RuntimeException("No moves available");
            
        List<TicTacToeMove> moveList = new ArrayList<>(moves);
        int index = random.nextInt(moveList.size());
        return moveList.get(index);
    }

    @Override
    public String toString() {
        return position().render();
    }

    public TicTacToeState(Position position) {
        this.random = new Random();
        this.position = position;
    }
    
    public TicTacToeState() {
        this.random = new Random();
        this.position = TicTacToe.startingPosition();
    }
    
    public TicTacToeState(TicTacToe game, Random random, Position position) {
        this.game = game;
        this.random = random;
        this.position = position;
    }

    private final Random random;
    private final Position position;
    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;
}