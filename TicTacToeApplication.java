package ticTacToe;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Play tic-tac-toe against computer
 * Choose to start x or o in title screen
 * If O is chosen, computer makes the first turn
*/

public class TicTacToeApplication extends Application {

    @Override
    public void start(Stage stage) {
        GameLogic gameLogic = new GameLogic();
        gameLogic.startGame(stage);
    }

    public static void main(String[] args) {
        Application.launch(TicTacToeApplication.class);
    }
    
}
