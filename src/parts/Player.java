package parts;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class Player {
    Piece playersPiece;

    public Player() {
        Piece p = new Piece(this);
        this.playersPiece = p;
    }

    public Piece getPlayersPiece() {
        return playersPiece;
    }
}
