package parts;

import graphics.GraphicsRenderer;
import javafx.stage.Stage;
import players.Player;

import java.io.FileNotFoundException;

/**
 * Created by liamkreiss on 4/1/19.
 */
public class VisualGame {
    private Board gameboard;
    private Player p1;
    private Player p2;
    private Player winner;
    private GraphicsRenderer gr;

    public VisualGame(Stage primarySage) throws FileNotFoundException {
        this.gr = new GraphicsRenderer(primarySage, this.gameboard);
    }

    public void setPlayers(Player p1, Player p2) {
        this.gameboard = new Board();
        this.p1 = p1;
        this.p2 = p2;

        gr.
    }

    public void runGame() {
        boolean p1Turn = true;
        Move curMove;
        int k; //k is the level that the move fell to, with 0 being the top and 3 being the bottom
        GameState gs;
        //first turn
        if (p1Turn) {
            curMove = p1.getMove(gameboard, p2);
            k = gameboard.playMove(curMove);
            gs = gameboard.getGameState(curMove, k);
        } else {
            throw new Error("p1 didn't go first");
        }

        while (!gs.isGameOver()) {
            p1Turn = !p1Turn;
            if (p1Turn) {
                curMove = p1.getMove(gameboard, p2);
            } else {
                curMove = p2.getMove(gameboard, p1);
            }
            k = gameboard.playMove(curMove);
            gs = gameboard.getGameState(curMove, k);
        }

        if (gs.isTie()) {
            winner = null;
            System.out.println("It's a tie!");
        } else {
            if (p1Turn) {
                winner = p1;
            } else {
                winner = p2;
            }
            System.out.println(gs.getWinner().toString() + " wins!");
        }

        System.out.println("Final Board State:\n" + gameboard.toString());

    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public Player getWinner() {
        return winner;
    }
}
