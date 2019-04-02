package graphics;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import parts.Board;
import parts.Game;
import players.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by liamkreiss on 4/1/19.
 */
public class GraphicsRenderer {
    private final boolean SAVE_TO_DATA = false;
    private static final String DATA_FILE = "sorted_data";
    private static final int CANVAS_WIDTH = 512;
    private static final int CANVAS_HEIGHT = 512;
    private final int NUMBER_OF_PLAYER_TYPES = 4;
    private final Difficulty[] PLAYER_TYPES = new Difficulty[]{Difficulty.HUMAN, Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD};
    private final Color BACKGROUND_COLOR = Color.GRAY;
    private final Color BOARD_COLOR = Color.BLACK;
    private final Color TILE_COLOR = Color.LIGHTBLUE;
    private final Color BUTTON_COLOR = Color.LIGHTBLUE;
    private final Color HIGHLIGHTED_COLOR = Color.YELLOW;
    private final Color TEXT_COLOR = Color.BLACK;
    private final Stage theStage;

    private int BOARD_SIZE;
    private int PIECE_SPACE_LENGTH;
    private int PIECE_RADIUS;
    private int BUTTON_WIDTH;
    private int BUTTON_HEIGHT;

    private Rectangle[][][] pieceSpaces;
    private Rectangle[] menuButtons;
    private Rectangle background;
    private Sphere[][][] gamePieces;
    private static HashMap<String, Boolean> gameInfo = new HashMap<String, Boolean>();


    private Player p1;
    private Player p2;
    private Board gameboard;


    public GraphicsRenderer(Stage primaryStage, Board gameboard) throws FileNotFoundException {

        this.theStage = primaryStage;
        theStage.setTitle("3D Connect Four");
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        initializeSizes(gameboard.getSize());

        createMenu();

        mainMenuScreen(theStage, theScene, gc);
    }

    private void initializeSizes(int boardSize) {
        this.BOARD_SIZE = boardSize;
        this.PIECE_SPACE_LENGTH = CANVAS_WIDTH / 10;
        this.PIECE_RADIUS = (PIECE_SPACE_LENGTH - 4) / 2;
        this.BUTTON_WIDTH = CANVAS_WIDTH / (NUMBER_OF_PLAYER_TYPES + 1);
        this.BUTTON_HEIGHT = CANVAS_HEIGHT / 4;
    }

    public void displayBoard() {

    }

    private void mainMenuScreen(Stage theStage, Scene theScene, GraphicsContext gc) {
        Player[] possiblePlayers = new Player[2];
        possiblePlayers[0] = null;
        possiblePlayers[1] = null;
        drawMainMenu(gc, possiblePlayers);
        theStage.show();

        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnButton", false);
        int[] buttonClickedOn = new int[1];

        theScene.setOnMouseClicked(
                e -> {
                    for (int i = 0; i < BOARD_SIZE; i++) {
                        if (menuButtons[i].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnButton", true);
                            buttonClickedOn[0] = i;
                        }
                    }

                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnButton")) {
                            Player newPlayer;
                            boolean playerGoesFirst = buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES == 0 ? true : false;
                            if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.HUMAN.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new Player("A");
                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.EASY.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new EasyComputer("B");
                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.MEDIUM.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new MediumComputer("C");
                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.HARD.getNumber()) {
                                try {
                                    possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new GeneticPlayer("D", "training15529636364.txt");
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                throw new Error("illegal click");
                            }
                            if (possiblePlayers[0] != null && possiblePlayers[1] != null) {
                                this.game = new Game(GameType.VISUAL_GAME, possiblePlayers[0], possiblePlayers[1]);
                                this.players = game.getPlayers();
                                createBoard();
                                boardScreen(theStage, theScene, gc, game);
                            } else {
                                drawMainMenu(gc, possiblePlayers);
                            }
                        }
                    }
                }
        );

    }

    private void drawMainMenu(GraphicsContext gc, Player[] possiblePlayers) {
        drawBackground(gc);
        gc.setFill(TEXT_COLOR);
        for (int i = 1; i <= 2; i++) {
            gc.setFill(TEXT_COLOR);
            gc.fillText(String.format("Who is player %d?", i), (CANVAS_WIDTH / 2) - 50, (CANVAS_HEIGHT / 14) * ((6 * i) - 5));
        }
        Rectangle button;
        StringAlignUtils util = new StringAlignUtils(20, Alignment.CENTER);
        for (int i = 0; i < NUMBER_OF_PLAYER_TYPES * 2; i++) {
            button = menuButtons[i];
            gc.setFill(getMenuButtonColor(i, button, players));
            gc.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
            gc.setFill(TEXT_COLOR);
            gc.fillText(util.format(String.format("%s", PLAYER_TYPES[i % NUMBER_OF_PLAYER_TYPES].toString())), button.getX() + 10, button.getY() + 50);
        }
    }
}
