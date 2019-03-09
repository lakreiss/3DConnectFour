package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import parts.Board;
import parts.GameState;
import parts.Move;
import parts.Player;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
        initializeGame();

    }

    private void initializeGame() {
        Board gameboard = new Board();
        Player p1 = new Player("A");
        Player p2 = new Player("B");

        startGame(gameboard, p1, p2);
    }

    private void startGame(Board gameboard, Player p1, Player p2) {
        boolean p1Turn = true;
        Move curMove;
        int k; //k is the level that the move fell to, with 0 being the top and 3 being the bottom
        GameState gs;
        //first turn
        if (p1Turn) {
            curMove = p1.getMove(gameboard);
            k = gameboard.playMove(curMove);
            gs = gameboard.getGameState(curMove, k);
        } else {
            throw new Error("p1 didn't go first");
        }

        while (!gs.isGameOver()) {
            p1Turn = !p1Turn;
            if (p1Turn) {
                curMove = p1.getMove(gameboard);
            } else {
                curMove = p2.getMove(gameboard);
            }
            k = gameboard.playMove(curMove);
            gs = gameboard.getGameState(curMove, k);
        }

        if (gs.isTie()) {
            System.out.println("It's a tie!");
        } else {
            System.out.println(gs.getWinner().toString() + " wins!");
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
