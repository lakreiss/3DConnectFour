package parts;

import players.EasyComputer;
import players.MediumComputer;
import players.Player;

/**
 * Created by liamkreiss on 3/10/19.
 */
public class Game {
    private Board gameboard;
    private Player p1;
    private Player p2;

    public Game() {
        this.gameboard = new Board();
//        this.p1 = new Player("A");
//        this.p1 = new EasyComputer("C");
        this.p1 = new MediumComputer("E");
//        this.p2 = new Player("B");
//        this.p2 = new EasyComputer("D");
        this.p2 = new MediumComputer("F");

        startGame();
    }

    public void startGame() {
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
            System.out.println("It's a tie!");
        } else {
            System.out.println(gs.getWinner().toString() + " wins!");
        }

        System.out.println("Final Board State:\n" + gameboard.toString());

    }
}
