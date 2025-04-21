package application;

import game.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class MainController {
    @FXML private GridPane boardGrid;
    @FXML private Label statusLabel;
    @FXML private Button resetButton;
    
    private Button[][] buttons;
    private TicTacToeState gameState;
    private TicTacToe game;
    private static final int GRID_SIZE = 3;
    private static final int EASY = 20;
    private static final int MEDIUM = 500;
    private static final int HARD = 100000;
    private static final int DIFFICULTY = EASY;  //Iteration times of MCTS
    @FXML
    public void initialize() {
        buttons = new Button[GRID_SIZE][GRID_SIZE];
        game = new TicTacToe(1000); // Fixed seed for consistent AI behavior
        
        // Initialize button grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button btn = new Button();
                btn.setPrefSize(100, 100);
                btn.setFont(Font.font(36));
                final int row = i, col = j;
                btn.setOnAction(e -> handleButtonClick(row, col));
                boardGrid.add(btn, j, i);
                buttons[i][j] = btn;
            }
        }
        resetButton.setOnAction(e -> resetGame());
        resetGame();
    }

    private void resetGame() {
        gameState = game.start();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setDisable(false);
            }
        }
        statusLabel.setText("Game Start! It's " + (gameState.player() == TicTacToe.X ? "you" : "machine") + " moving");
        if (gameState.player() == TicTacToe.O) makeComputerMove();
    }

    private void handleButtonClick(int row, int col) {
        if (!buttons[row][col].getText().isEmpty() || gameState.isTerminal()) return;
        
        TicTacToeMove move = new TicTacToeMove(TicTacToe.X, row, col);
        gameState = gameState.next(move);
        updateBoard();
        
        if (gameState.isTerminal()) {
            endGame();
            return;
        }
        makeComputerMove();
    }

    private void makeComputerMove() {
        MCTS mcts = new MCTS(new TicTacToeNode(gameState), DIFFICULTY);
        TicTacToeMove bestMove = mcts.getBestMove();
        gameState = gameState.next(bestMove);
        updateBoard();
        
        if (gameState.isTerminal()) endGame();
        else statusLabel.setText("Your turn!");
    }

    private void updateBoard() {
        Position position = gameState.position();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int cell = position.grid[i][j];
                buttons[i][j].setText(cell == TicTacToe.O ? "O" : cell == TicTacToe.X ? "X" : "");
            }
        }
    }

    private void endGame() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setDisable(true);
            }
        }
        
        if (gameState.winner().isPresent()) {
            int winner = gameState.winner().get();
            statusLabel.setText(winner == TicTacToe.X ? "You win" : "Machine wins!");
        } else {
            statusLabel.setText("Draw");
        }
    }
}