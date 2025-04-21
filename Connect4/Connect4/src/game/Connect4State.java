package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Connect4State {
    Connect4 game;
    private Random random;
    private final Connect4Position position;
    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;

    public Connect4 game() {
        return this.game;
    }

    public int player() {
        return switch (position.last) {
            case 0 -> X;
            case 1, -1 -> O;
            default -> blank;
        };
    }

    public Connect4Position position() {
        return this.position;
    }

    public Optional<Integer> winner() {
        return position.winner();
    }

    public Random random() {
        return random;
    }

    public Collection<Connect4Move> moves(int player) {
        if (player == position.last) throw new RuntimeException("consecutive moves by same player: " + player);
        List<Integer> moves = position.moves(player);
        ArrayList<Connect4Move> list = new ArrayList<>();
        for (int column : moves) list.add(new Connect4Move(player, column));
        return list;
    }

    public Connect4State next(Connect4Move move) {
        int column = move.move();
        return new Connect4State(game, random, position.move(move.player(), column));
    }

    public boolean isTerminal() {
        return position.full() || position.winner().isPresent();
    }

    public Connect4Move chooseMove(int player) {
        Collection<Connect4Move> moves = moves(player);
        if (moves.isEmpty()) 
            throw new RuntimeException("No moves available");
            
        List<Connect4Move> moveList = new ArrayList<>(moves);
        int index = random.nextInt(moveList.size());
        return moveList.get(index);
    }

    @Override
    public String toString() {
        return position().render();
    }

    public Connect4State(Connect4Position position) {
        this.random = new Random();
        this.position = position;
    }
    
    public Connect4State() {
        this.random = new Random();
        this.position = Connect4.startingPosition();
    }
    
    public Connect4State(Connect4 game, Random random, Connect4Position position) {
        this.game = game;
        this.random = random;
        this.position = position;
    }
}
