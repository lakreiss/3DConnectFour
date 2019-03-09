package parts;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class Move {
    private int row;
    private int col;
    private Piece piece;

    public Move(int row, int col, Piece p) {
        this.row = row;
        this.col = col;
        this.piece = p;

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }
}
