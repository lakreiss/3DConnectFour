package players;

import parts.Board;
import parts.Move;

import java.util.Random;

/**
 * Created by liamkreiss on 3/10/19.
 */
public class EasyComputer extends ComputerPlayer {
    private Random rand = new Random();

    public EasyComputer() {
        super();
    }

    public EasyComputer(String name) {
        super(name);
    }

    public Move getMove(Board gameboard, Player opponent) {
        int row = rand.nextInt(gameboard.getSize());
        int col = rand.nextInt(gameboard.getSize());

        Move m = new Move(row, col, this.getPlayersPiece());
        return gameboard.isLegalMove(m) ? m : getMove(gameboard, opponent);
    }
}
