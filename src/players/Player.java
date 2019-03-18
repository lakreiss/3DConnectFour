package players;

import parts.Board;
import parts.Move;
import parts.Piece;

import java.util.Scanner;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class Player {
    protected Piece playersPiece;
    protected String name;

    public Player(String name) {
        Piece p = new Piece(this);
        this.playersPiece = p;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Piece getPlayersPiece() {
        return playersPiece;
    }

    //each instance of Player should implement this
    public Move getMove(Board gameboard, Player opponent) {
        System.out.println("Current Board:");
        System.out.printf(gameboard.toString());
        Scanner console = new Scanner(System.in);
        System.out.printf("%s, what row would you like to place your piece in? (A, B, C, or D) ", this.name);
        int row = console.nextLine().toLowerCase().charAt(0) - ((int) 'a');

        System.out.printf("%s, what column would you like to place your piece in? (1, 2, 3, or 4) ", this.name);
        int col;
        if (console.hasNextInt()) {
            col = console.nextInt() - 1;
        } else {
            return getMove(gameboard, opponent);
        }

        Move move = new Move(row, col, this.playersPiece);
        if (gameboard.isLegalMove(move)) {
            return move;
        } else {
            System.out.println("Illegal entry. Please start over.");
            return getMove(gameboard, opponent);
        }
    }

    public String toString() {
        return this.name;
    }
}
