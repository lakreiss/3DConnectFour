package parts;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class PieceSpace {
    boolean occupied;
    Piece piece;

    public PieceSpace() {
        this.occupied = false;
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        this.occupied = true;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Player getPlayer() {
        if (isOccupied()) {
            return piece.getOwner();
        } else {
            return null;
        }
    }

    public String toString() {
        if (!occupied) {
            return "0";
        } else {
            return piece.getOwner().toString();
        }
    }
}
