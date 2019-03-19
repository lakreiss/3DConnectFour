package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import parts.Board;
import parts.Game;
import players.EasyComputer;
import players.GeneticPlayer;
import players.Player;

import java.io.FileNotFoundException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

//        initializeGame();

        String trainingFile = "training15529636364.txt";
        initializeTraining(trainingFile, 20);

//        playGeneticAlgorithm();
    }

    private void playGeneticAlgorithm() throws FileNotFoundException {
        Player p1 = new Player("A");
        GeneticPlayer p2 = new GeneticPlayer("B", "training15529636364.txt");
        Game g = new Game(p1, p2);
    }

    private void initializeGame() {
        Player p1 = new Player("A");
//        Player p1 = new EasyComputer("C");


        Player p2 = new Player("B");
//        Player p2 = new EasyComputer("D");

        Game g = new Game(p1, p2);
//        g.startGame();
    }

    private void initializeTraining(String trainingFile, int generations) throws FileNotFoundException {
        new TrainGeneticAlgorithm(trainingFile, generations);
    }




    public static void main(String[] args) {
        launch(args);

    }
}
