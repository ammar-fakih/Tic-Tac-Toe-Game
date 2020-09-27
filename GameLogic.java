package ticTacToe;

import java.util.concurrent.ThreadLocalRandom;
import java.awt.Point;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameLogic {
    private Label turnLabel;
    private HashMap<Point, Button> squares;
    private int turn;
    
    //GameLogic constructor. X goes first
    public GameLogic() {
        this.squares = new HashMap<>();
        this.turnLabel = new Label("Turn: X");
    }
    
    //game starts when method is called.
    public void startGame(Stage stage) {
        //Buttons are placed in a grid pane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        
        //9 buttons are created by calling createButton()
        //Then, they are put in the hashmap. The key is the same coordinate as
        //in the gridpane
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                Button button = createButton();
                squares.put(new Point(i,j), button);
                
                grid.add(button, i, j);
            }
        }
        
        //Startover button, placed in StackPane
        Button startOver = new Button("Start Over");    
        StackPane bottomLayout = new StackPane(startOver);
        StackPane.setMargin(startOver, new Insets(10,10,10,10));
        this.turnLabel.setFont(Font.font("Arial", 40));
        
        //Borderpane layout for game scene
        BorderPane layout = new BorderPane();
        layout.setCenter(grid);
        layout.setTop(this.turnLabel);
        layout.setBottom(bottomLayout);
        BorderPane.setAlignment(this.turnLabel, Pos.CENTER);
        BorderPane.setAlignment(startOver, Pos.CENTER);
        
        //Create start buttons and place in VBox layout
        Button startX = new Button("Start X");
        Button startO = new Button("Start O");
        VBox titleButtons = new VBox();
        titleButtons.getChildren().addAll(startX, startO);
        titleButtons.setAlignment(Pos.CENTER);
        VBox.setMargin(titleButtons, new Insets(20,20,20,20));
        titleButtons.setSpacing(10);
        
        //Borderpane layout for title scene
        Label title = new Label("Tic-Tac-Toe");
        title.setFont(Font.font("Arial", 40));
        BorderPane titleScreen = new BorderPane();
        titleScreen.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        titleScreen.setCenter(titleButtons);
        
        Scene gameScene = new Scene(layout);
        
        //Event handlers for start buttons
        startX.setOnAction((event) -> {
            this.turn = 0;
            stage.setScene(gameScene);
            computerTurn();
        });
        startO.setOnAction((event) -> {
            this.turn = 1;
            stage.setScene(gameScene);
            computerTurn();
        });
        
        Scene titleScene = new Scene(titleScreen);
        stage.setScene(titleScene);
        
        //Start over button resets button text to " " and changes scene to title
        startOver.setOnAction((event) -> {
            for (int r = 0; r <= 2; r++) {
                for (int c = 0; c <= 2; c++) {
                    Point point =  new Point(r,c);
                    this.squares.get(point).setText(" ");
                }
            }
            this.turnLabel.setText("Turn: X");
            stage.setScene(titleScene);
        });
        
        stage.show();
    }
    
    //create new button with font Monospaced
    //creates event handler for click
    //increments turn to switch between comp and user
    private Button createButton() {
        Button button = new Button(" ");
        button.setFont(Font.font("Monospaced", 40));
        
        button.setOnAction((event) -> {
            this.turn++;
            makeTurn(button);
        });
        return button;
    }
    
    //called after each button click. only calls makeTurn if this.turn is odd
    private void computerTurn() {
        
        if (this.turn == 1 || this.turn % 2 == 1) {
            //uses ThreadLocalRandom to choose random int between 0 and 3 for 2 times
            int x = ThreadLocalRandom.current().nextInt(0, 3);
            int y = ThreadLocalRandom.current().nextInt(0, 3);
            Point point = new Point(x, y);
            //only continues past while loop if button at point chosen is empty
            while (this.squares.get(point).getText().equals("X") 
                    || this.squares.get(point).getText().equals("O")) {
                x = ThreadLocalRandom.current().nextInt(0, 3);
                y = ThreadLocalRandom.current().nextInt(0, 3);
                point = new Point(x, y);
            }
            //fires button that is randomly chosen
            this.squares.get(point).fire();
        }
        
    }
    
    
     public void makeTurn(Button button) {
         //nothings happens if user choses already chosen button
        if ((button.getText().equals("X") || button.getText().equals("O"))) {
            return;
        }
        //Label is split into parts to retrieve X or O
        String[] parts = this.turnLabel.getText().split(" ");
        
        //if it is x turn, change button to X, and vice versa
        //after, check if game has ended and change label accordingly
        //call computerTurn() if game hasnt ended
        if (parts[1].equals("X")) {
            button.setText("X");
            if (checkEnd() == 0) {
                this.turnLabel.setText("Turn: O");
                computerTurn();
            } else if (checkEnd() == 1) {
                this.turnLabel.setText("X Won!");
            } else {
                this.turnLabel.setText("O Won!");
            }
        } else if (parts[1].equals("O")) {
            button.setText("O");
            if (checkEnd() == 0) {
                this.turnLabel.setText("Turn: X");
                computerTurn();
            } else if (checkEnd() == 1) {
                this.turnLabel.setText("X Won!");
            } else {
                this.turnLabel.setText("O Won!");
            }
        }
    }
    
    //check if game has ended and return byte (0 if not ended, 1 if X won, and 
    //2 if O won.
    private byte checkEnd() {
        int counter = 2;
        StringBuilder diagonal1 = new StringBuilder();
        StringBuilder diagonal2 = new StringBuilder();
        for (int r = 0; r <= 2; r++) {
            StringBuilder rowTotal = new StringBuilder();
            StringBuilder colTotal = new StringBuilder();
            String x = "XXX";
            String o = "OOO";
            
            for (int c = 0; c <= 2; c++) {
                Point colPoint = new Point(r, c);
                Point rowPoint = new Point(c, r);
                rowTotal.append(this.squares.get(rowPoint).getText());
                colTotal.append(this.squares.get(colPoint).getText());
                
                if (r == c) {
                    diagonal1.append(this.squares.get(rowPoint).getText());
                }
                if (c == counter) {
                    diagonal2.append(this.squares.get(rowPoint).getText());
                }
            }
            
            if (rowTotal.toString().equals(x) || diagonal1.toString().equals(x) 
                    || colTotal.toString().equals(x) || diagonal2.toString().equals(x)) {
                return 1;
            }
            if (rowTotal.toString().equals(o) || diagonal1.toString().equals(o) 
                    || colTotal.toString().equals(o) || diagonal2.toString().equals(o)) {
                return 2;
            }
            counter--;
        }
     
        return 0;
    }
}