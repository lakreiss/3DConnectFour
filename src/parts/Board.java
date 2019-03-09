package parts;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class Board {
    private static final int BOARD_DIM = 4;
    private PieceSpace[][][] board_pieces;

    /*
             A   B   C   D (j)
 (i)     1   |   |   |   |          0
      2   |  ||  ||  ||  |          1
   3   |  || ||| ||| ||  |          2 (k)
4   |  || |||||||||| ||  |          3
    |  || ||| ||| ||  |     /
    |  ||  ||  ||  |     /
    |   |   |   |     /
___________________/

     */

    public Board() {
        board_pieces = new PieceSpace[BOARD_DIM][BOARD_DIM][BOARD_DIM];
        for (int i = 0; i < board_pieces.length; i++) {
            for (int j = 0; j < board_pieces[0].length; j++) {
                for (int k = 0; k < board_pieces[0][0].length; k++) {
                    board_pieces[i][j][k] = new PieceSpace();
                }
            }
        }
    }

    public int playPiece(Piece p, int i, int j) {
        if (board_pieces[i][j][0].isOccupied()) {
            return -1;
        } else {
            int lowest_row = 0;
            while (lowest_row < BOARD_DIM - 1 && !board_pieces[i][j][lowest_row + 1].isOccupied()) {
                lowest_row++;
            }
            board_pieces[i][j][lowest_row].setPiece(p);
            return lowest_row;
        }
    }

    public GameState getGameOver(int i, int j, int k) {
        //checks if i,j,k created a victory for player at i,j,k

        boolean potentialWinnerWon = false;
        Player potentialWinner = board_pieces[i][j][k].getPlayer();

        if (isTop(k)) {
            potentialWinnerWon = checkColumn(potentialWinner, i, j);
            if (!potentialWinnerWon) {
                potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
            }
            if (!potentialWinnerWon && isTopCorner(i, j)) {
                potentialWinnerWon = checkTopCorner(potentialWinner, i, j);
            } else if (!potentialWinnerWon && isTopEdge(i, j)) {
                potentialWinnerWon = checkTopEdge(potentialWinner, i, j);
            }
        } else if (isBottom(k)) {
            potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
            if (!potentialWinnerWon && isBottomCorner(i, j)) {
                potentialWinnerWon = checkBottomCorner(potentialWinner, i, j);
            } else if (!potentialWinnerWon && isBottomEdge(i, j)) {
                potentialWinnerWon = checkBottomEdge(potentialWinner, i, j);
            }
        } else {
            potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
            if (!potentialWinnerWon && isMiddleEdge(i, j)) {
                potentialWinnerWon = checkMiddleEdge(potentialWinner, i, j, k);
            } else if (!potentialWinnerWon && isMiddleMiddle(i, j)) {
                potentialWinnerWon = checkMiddleMiddle(potentialWinner, i, j, k);
            }
        }

        if (potentialWinnerWon) {
            return new GameState(true, potentialWinner);
        }


        //if k is at 0 (top row), checks if board is full, in which case it's a tie
        boolean board_full = true;
        for (int row = 0; row < BOARD_DIM; row++) {
            for (int col = 0; col < BOARD_DIM; col++) {
                if (!board_pieces[row][col][0].isOccupied()) {
                    board_full = false;
                    break;
                }
            }
        }
        if (board_full) {
            return new GameState(true, null);
        } else {
            return new GameState(false, null);
        }
    }
}
