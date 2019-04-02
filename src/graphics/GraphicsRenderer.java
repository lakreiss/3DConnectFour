package graphics;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
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

        test(primaryStage);

//        createMenu();
//        mainMenuScreen(theStage, theScene, gc);
    }

    public void test(Stage stage) {
        //Drawing Cylinder1
        Cylinder cylinder1 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder1.setHeight(130.0f);
        cylinder1.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder1.setTranslateX(100);
        cylinder1.setTranslateY(75);

        //Preparing the phong material of type bump map
        PhongMaterial material1 = new PhongMaterial();
        material1.setBumpMap(new Image
                ("http://www.tutorialspoint.com/images/tplogo.gif"));

        //Setting the bump map material to Cylinder1
        cylinder1.setMaterial(material1);

        //Drawing Cylinder2
        Cylinder cylinder2 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder2.setHeight(130.0f);
        cylinder2.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder2.setTranslateX(200);
        cylinder2.setTranslateY(75);

        //Preparing the phong material of type diffuse map
        PhongMaterial material2 = new PhongMaterial();
        material2.setDiffuseMap(new Image
                ("http://www.tutorialspoint.com/images/tp-logo.gif"));

        //Setting the diffuse map material to Cylinder2
        cylinder2.setMaterial(material2);

        //Drawing Cylinder3
        Cylinder cylinder3 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder3.setHeight(130.0f);
        cylinder3.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder3.setTranslateX(300);
        cylinder3.setTranslateY(75);

        //Preparing the phong material of type Self Illumination Map
        PhongMaterial material3 = new PhongMaterial();
        material3.setSelfIlluminationMap(new Image
                ("http://www.tutorialspoint.com/images/tp-logo.gif"));

        //Setting the Self Illumination Map material to Cylinder3
        cylinder3.setMaterial(material3);

        //Drawing Cylinder4
        Cylinder cylinder4 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder4.setHeight(130.0f);
        cylinder4.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder4.setTranslateX(400);
        cylinder4.setTranslateY(75);

        //Preparing the phong material of type Specular Map
        PhongMaterial material4 = new PhongMaterial();
        material4.setSpecularMap(new Image
                ("http://www.tutorialspoint.com/images/tp-logo.gif"));

        //Setting the Specular Map material to Cylinder4
        cylinder4.setMaterial(material4);

        //Drawing Cylinder5
        Cylinder cylinder5 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder5.setHeight(130.0f);
        cylinder5.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder5.setTranslateX(100);
        cylinder5.setTranslateY(300);

        //Preparing the phong material of type diffuse color
        PhongMaterial material5 = new PhongMaterial();
        material5.setDiffuseColor(Color.BLANCHEDALMOND);

        //Setting the diffuse color material to Cylinder5
        cylinder5.setMaterial(material5);

        //Drawing Cylinder6
        Cylinder cylinder6 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder6.setHeight(130.0f);
        cylinder6.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder6.setTranslateX(200);
        cylinder6.setTranslateY(300);

        //Preparing the phong material of type specular color
        PhongMaterial material6 = new PhongMaterial();

        //setting the specular color map to the material
        material6.setSpecularColor(Color.BLANCHEDALMOND);

        //Setting the specular color material to Cylinder6
        cylinder6.setMaterial(material6);

        //Drawing Cylinder7
        Cylinder cylinder7 = new Cylinder();

        //Setting the properties of the Cylinder
        cylinder7.setHeight(130.0f);
        cylinder7.setRadius(30.0f);

        //Setting the position of the Cylinder
        cylinder7.setTranslateX(300);
        cylinder7.setTranslateY(300);

        //Preparing the phong material of type Specular Power
        PhongMaterial material7 = new PhongMaterial();
        material7.setSpecularPower(0.1);

        //Setting the Specular Power material to the Cylinder
        cylinder7.setMaterial(material7);

        //Creating a Group object
        Group root = new Group(cylinder1 ,cylinder2, cylinder3,
                cylinder4, cylinder5, cylinder6, cylinder7);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        //Setting camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(-10);
        scene.setCamera(camera);

        //Setting title to the Stage
        stage.setTitle("Drawing a cylinder");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
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

//    private void mainMenuScreen(Stage theStage, Scene theScene, GraphicsContext gc) {
//        Player[] possiblePlayers = new Player[2];
//        possiblePlayers[0] = null;
//        possiblePlayers[1] = null;
//        drawMainMenu(gc, possiblePlayers);
//        theStage.show();
//
//        HashMap<String, Boolean> clickInfo = new HashMap<>();
//        clickInfo.put("clicked", false);
//        clickInfo.put("clickedOnButton", false);
//        int[] buttonClickedOn = new int[1];
//
//        theScene.setOnMouseClicked(
//                e -> {
//                    for (int i = 0; i < BOARD_SIZE; i++) {
//                        if (menuButtons[i].contains(e.getX(), e.getY())) {
//                            clickInfo.put("clicked", true);
//                            clickInfo.put("clickedOnButton", true);
//                            buttonClickedOn[0] = i;
//                        }
//                    }
//
//                    if (clickInfo.get("clicked")) {
//                        if (clickInfo.get("clickedOnButton")) {
//                            Player newPlayer;
//                            boolean playerGoesFirst = buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES == 0 ? true : false;
//                            if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.HUMAN.getNumber()) {
//                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new Player("A");
//                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.EASY.getNumber()) {
//                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new EasyComputer("B");
//                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.MEDIUM.getNumber()) {
//                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new MediumComputer("C");
//                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.HARD.getNumber()) {
//                                try {
//                                    possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new GeneticPlayer("D", "training15529636364.txt");
//                                } catch (FileNotFoundException e1) {
//                                    e1.printStackTrace();
//                                }
//                            } else {
//                                throw new Error("illegal click");
//                            }
//                            if (possiblePlayers[0] != null && possiblePlayers[1] != null) {
//                                this.game = new Game(GameType.VISUAL_GAME, possiblePlayers[0], possiblePlayers[1]);
//                                this.players = game.getPlayers();
//                                createBoard();
//                                boardScreen(theStage, theScene, gc, game);
//                            } else {
//                                drawMainMenu(gc, possiblePlayers);
//                            }
//                        }
//                    }
//                }
//        );
//
//    }
//
//    private void drawMainMenu(GraphicsContext gc, Player[] possiblePlayers) {
//        drawBackground(gc);
//        gc.setFill(TEXT_COLOR);
//        for (int i = 1; i <= 2; i++) {
//            gc.setFill(TEXT_COLOR);
//            gc.fillText(String.format("Who is player %d?", i), (CANVAS_WIDTH / 2) - 50, (CANVAS_HEIGHT / 14) * ((6 * i) - 5));
//        }
//        Rectangle button;
//        StringAlignUtils util = new StringAlignUtils(20, Alignment.CENTER);
//        for (int i = 0; i < NUMBER_OF_PLAYER_TYPES * 2; i++) {
//            button = menuButtons[i];
//            gc.setFill(getMenuButtonColor(i, button, players));
//            gc.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
//            gc.setFill(TEXT_COLOR);
//            gc.fillText(util.format(String.format("%s", PLAYER_TYPES[i % NUMBER_OF_PLAYER_TYPES].toString())), button.getX() + 10, button.getY() + 50);
//        }
//    }
}
