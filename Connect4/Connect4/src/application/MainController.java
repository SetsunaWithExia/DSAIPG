package application;

import game.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML private GridPane boardGrid;
    @FXML private Label statusLabel;
    @FXML private Button resetButton;
    
    private StackPane[][] cells;
    private Circle[][] pieces;
    private Connect4State gameState;
    private Connect4 game;
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int EASY = 20;
    private static final int MEDIUM = 100;
    private static final int HARD = 1000;
    private static final int DIFFICULTY = HARD;
    @FXML
    public void initialize() {
        cells = new StackPane[ROWS][COLS];
        pieces = new Circle[ROWS][COLS];
        game = new Connect4(1000);
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("game-cell");
                
                Circle piece = new Circle(25);
                piece.getStyleClass().add("game-piece-empty");
                
                cell.getChildren().add(piece);
                final int column = col;
                cell.setOnMouseClicked(e -> handleColumnClick(column));
                
                boardGrid.add(cell, col, row);
                cells[row][col] = cell;
                pieces[row][col] = piece;
            }
        }
        
        resetButton.setOnAction(e -> resetGame());
        resetGame();
    }
    
    private void resetGame() {
        gameState = game.start();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                pieces[row][col].getStyleClass().clear();
                pieces[row][col].getStyleClass().add("game-piece-empty");
            }
        }
        statusLabel.setText("Game started! " + (gameState.player() == Connect4.X ? "Your" : "Computer's") + " turn");
        
        if (gameState.player() == Connect4.O) {
            makeComputerMove();
        }
    }
    
    private void handleColumnClick(int col) {
        if (gameState.isTerminal()) return;
        if (gameState.player() != Connect4.X) return;
        
        try {
            Connect4Move move = new Connect4Move(Connect4.X, col);
            gameState = gameState.next(move);
            updateBoard();
            
            if (gameState.isTerminal()) {
                endGame();
                return;
            }
            
            makeComputerMove();
        } catch (RuntimeException e) {
            statusLabel.setText("Invalid move! Column is full.");
        }
    }
    
    private void makeComputerMove() {
        statusLabel.setText("Computer is thinking...");
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                
                Platform.runLater(() -> {
                    MCTS mcts = new MCTS(new Connect4Node(gameState), DIFFICULTY);
                    Connect4Move bestMove = mcts.getBestMove();
                    
                    gameState = gameState.next(bestMove);
                    updateBoard();
                    
                    if (gameState.isTerminal()) {
                        endGame();
                    } else {
                        statusLabel.setText("Your turn");
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void updateBoard() {
        Connect4Position position = gameState.position();
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int cell = position.grid[row][col];
                pieces[row][col].getStyleClass().clear();
                
                if (cell == Connect4.O) {
                    pieces[row][col].getStyleClass().add("game-piece-o");
                } else if (cell == Connect4.X) {
                    pieces[row][col].getStyleClass().add("game-piece-x");
                } else {
                    pieces[row][col].getStyleClass().add("game-piece-empty");
                }
            }
        }
    }
    
    private void endGame() {
        if (gameState.winner().isPresent()) {
            int winner = gameState.winner().get();
            if (winner == Connect4.X) {
                statusLabel.setText("Congratulations! You win!");
            } else {
                statusLabel.setText("Computer wins! Try again?");
            }
        } else {
            statusLabel.setText("It's a draw!");
        }
        resetButton.setStyle("-fx-background-color: #5436da;");
    }
}