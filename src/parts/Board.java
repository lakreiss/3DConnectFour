package parts;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class Board {
    private static final int BOARD_DIM = 4;
    private PieceSpace[][][] board_pieces;

    /*
             1   2   3   4 (j)
 (i)     A   |   |   |   |          0
      B   |  ||  ||  ||  |          1
   C   |  || ||| ||| ||  |          2 (k)
D   |  || |||||||||| ||  |          3
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

    public int playMove(Move move) {
        int i = move.getRow();
        int j = move.getCol();
        Piece p = move.getPiece();
        if (isLegalMove(move)) {
            int lowest_row = 0;
            while (lowest_row < BOARD_DIM - 1 && !board_pieces[i][j][lowest_row + 1].isOccupied()) {
                lowest_row++;
            }
            board_pieces[i][j][lowest_row].setPiece(p);
            return lowest_row;
        } else {
            return -1;
        }
    }

    public boolean isLegalMove(Move move) {
        if (move.getRow() < 0 || move.getRow() >= BOARD_DIM) {
            return false;
        }
        if (move.getCol() < 0 || move.getCol() >= BOARD_DIM) {
            return false;
        }
        if (board_pieces[move.getRow()][move.getCol()][0].isOccupied()) {
            return false;
        }
        return true;
    }

    public GameState getGameState(Move move, int k) {
        //checks if i,j,k created a victory for player at i,j,k
        int i = move.getRow();
        int j = move.getCol();

        return new GameState(false, null);

//        boolean potentialWinnerWon = false;
//        Player potentialWinner = board_pieces[i][j][k].getPlayer();
//
//        if (isTop(k)) {
//            potentialWinnerWon = checkColumn(potentialWinner, i, j);
//            if (!potentialWinnerWon) {
//                potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
//            }
//            if (!potentialWinnerWon && isTopCorner(i, j)) {
//                potentialWinnerWon = checkTopCorner(potentialWinner, i, j);
//            } else if (!potentialWinnerWon && isTopEdge(i, j)) {
//                potentialWinnerWon = checkTopEdge(potentialWinner, i, j);
//            }
//        } else if (isBottom(k)) {
//            potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
//            if (!potentialWinnerWon && isBottomCorner(i, j)) {
//                potentialWinnerWon = checkBottomCorner(potentialWinner, i, j);
//            } else if (!potentialWinnerWon && isBottomEdge(i, j)) {
//                potentialWinnerWon = checkBottomEdge(potentialWinner, i, j);
//            }
//        } else {
//            potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
//            if (!potentialWinnerWon && isMiddleEdge(i, j)) {
//                potentialWinnerWon = checkMiddleEdge(potentialWinner, i, j, k);
//            } else if (!potentialWinnerWon && isMiddleMiddle(i, j)) {
//                potentialWinnerWon = checkMiddleMiddle(potentialWinner, i, j, k);
//            }
//        }
//
//        if (potentialWinnerWon) {
//            return new GameState(true, potentialWinner);
//        }
//
//
//        //if k is at 0 (top row), checks if board is full, in which case it's a tie
//        boolean board_full = true;
//        for (int row = 0; row < BOARD_DIM; row++) {
//            for (int col = 0; col < BOARD_DIM; col++) {
//                if (!board_pieces[row][col][0].isOccupied()) {
//                    board_full = false;
//                    break;
//                }
//            }
//        }
//        if (board_full) {
//            return new GameState(true, null);
//        } else {
//            return new GameState(false, null);
//        }
    }

    public String toString() {
        String output = "";
        output += "Top\n";
        for (int k = 0; k < BOARD_DIM; k++) {
            output += "LEVEL " + k + "\n";
            for (int row = 0; row < BOARD_DIM; row++) {
                if (row == 0) {
                    output += ("  1234\n");
                }
                output += (char) ('A' + row) + " ";
                for (int col = 0; col < BOARD_DIM; col++) {
                    output += board_pieces[row][col][k];
                }
                output += "\n";
            }
            output += "\n";
        }
        return output;
    }
}
