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

        boolean potentialWinnerWon = false;
        Player potentialWinner = board_pieces[i][j][k].getPlayer();

        if (isTop(k)) {
            potentialWinnerWon = checkColumn(potentialWinner, i, j);
            if (!potentialWinnerWon) {
                potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
            }
            if (!potentialWinnerWon && isCorner(i, j)) {
                potentialWinnerWon = checkTopCorner(potentialWinner, i, j);
            } else if (!potentialWinnerWon && isEdge(i, j)) {
                potentialWinnerWon = checkTopEdge(potentialWinner, i, j);
            }
        } else if (isBottom(k)) {
            potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
            if (!potentialWinnerWon && isCorner(i, j)) {
                potentialWinnerWon = checkBottomCorner(potentialWinner, i, j);
            } else if (!potentialWinnerWon && isEdge(i, j)) {
                potentialWinnerWon = checkBottomEdge(potentialWinner, i, j);
            }
        } else {
            potentialWinnerWon = checkLevel(potentialWinner, i, j, k);
            if (!potentialWinnerWon && isEdge(i, j)) {
                potentialWinnerWon = checkMiddleEdge(potentialWinner, i, j);
            } else if (!potentialWinnerWon && !isEdge(i, j) && !isCorner(i, j)) {
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

    private boolean checkMiddleMiddle(Player pw, int row, int col, int level) {
        int cornerDiagRow = (row == level) ? 0 : 3;
        int cornerDiagCol = (col == level) ? 0 : 3;
        boolean diagCornerWin = checkTopCorner(pw, cornerDiagRow, cornerDiagCol);

        int edgeDiagRow1 = (row == level) ? 0 : 3;
        int edgeDiagCol1 = (col == 1) ? 1 : 2;
        boolean edgeWin1 = checkTopEdge(pw, edgeDiagRow1, edgeDiagCol1);

        int edgeDiagRow2 = (row == 1) ? 1 : 2;
        int edgeDiagCol2 = (col == level)? 0 : 3;
        boolean edgeWin2 = checkTopEdge(pw, edgeDiagRow2, edgeDiagCol2);

        return (diagCornerWin || edgeWin1 || edgeWin2);
    }

    private boolean checkMiddleEdge(Player potentialWinner, int row, int col) {
        boolean foundWinDiag1 = true;
        boolean foundWinDiag2 = true;

        for (int k = 0; k < BOARD_DIM - 1; k++) {
            if (row == 0 || row == BOARD_DIM - 1) {
                if (board_pieces[row][k][k].getPlayer() != potentialWinner) {
                    foundWinDiag1 = false;
                }
                if (board_pieces[row][BOARD_DIM - 1 - k][k].getPlayer() != potentialWinner) {
                    foundWinDiag2 = false;
                }
            } else if (col == 0 || col == BOARD_DIM - 1) {
                if (board_pieces[k][row][k].getPlayer() != potentialWinner) {
                    foundWinDiag1 = false;
                }
                if (board_pieces[BOARD_DIM - 1 - k][col][k].getPlayer() != potentialWinner) {
                    foundWinDiag2 = false;
                }
            } else {
                throw new Error("not a middle edge");
            }
        }
        return (foundWinDiag1 || foundWinDiag2);
    }

    private boolean checkBottomEdge(Player potentialWinner, int row, int col) {
        for (int k = BOARD_DIM - 1; k >= 0; k--) {
            if (row == 0) {
                if (board_pieces[row + k][col][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else if (row == BOARD_DIM - 1) {
                if (board_pieces[row - k][col][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else if (col == 0) {
                if (board_pieces[row][col + k][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else if (col == BOARD_DIM - 1) {
                if (board_pieces[row][col - k][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else {
                throw new Error("not bottom edge");
            }
        }
        return true;
    }

    private boolean checkTopEdge(Player potentialWinner, int row, int col) {
        for (int k = 0; k < BOARD_DIM; k++) {
            if (row == 0) {
                if (board_pieces[row + k][col][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else if (row == BOARD_DIM - 1) {
                if (board_pieces[row - k][col][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else if (col == 0) {
                if (board_pieces[row][col + k][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else if (col == BOARD_DIM - 1) {
                if (board_pieces[row][col - k][k].getPlayer() != potentialWinner) {
                    return false;
                }
            } else {
                throw new Error("not top edge");
            }
        }
        return true;
    }

    private boolean checkBottomCorner(Player potentialWinner, int row, int col) {
        boolean foundWinDiag = true;
        boolean foundWinRow = true;
        boolean foundWinCol = true;
        if (row == 0) {
            if (col == 0) {
                for (int k = BOARD_DIM - 1; k >= 0; k--) {
                    if (board_pieces[row + k][col + k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col + k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row + k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else if (col == BOARD_DIM - 1) {
                for (int k = BOARD_DIM - 1; k >= 0; k--) {
                    if (board_pieces[row + k][col - k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col - k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row + k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else {
                throw new Error("not a bottom corner 1");
            }
        } else if (row == BOARD_DIM - 1) {
            if (col == 0) {
                for (int k = BOARD_DIM - 1; k >= 0; k--) {
                    if (board_pieces[row - k][col + k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col + k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row - k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else if (col == BOARD_DIM - 1) {
                for (int k = BOARD_DIM - 1; k >= 0; k--) {
                    if (board_pieces[row - k][col - k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col - k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row - k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else {
                throw new Error("not a bottom corner 2");
            }
        } else {
            throw new Error("not a bottom corner 3");
        }
        return false;
    }

    private boolean checkTopCorner(Player potentialWinner, int row, int col) {
        boolean foundWinDiag = true;
        boolean foundWinRow = true;
        boolean foundWinCol = true;
        if (row == 0) {
            if (col == 0) {
                for (int k = 0; k < BOARD_DIM; k++) {
                    if (board_pieces[row + k][col + k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col + k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row + k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else if (col == BOARD_DIM - 1) {
                for (int k = 0; k < BOARD_DIM; k++) {
                    if (board_pieces[row + k][col - k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col - k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row + k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else {
                throw new Error("not a top corner 1");
            }
        } else if (row == BOARD_DIM - 1) {
            if (col == 0) {
                for (int k = 0; k < BOARD_DIM; k++) {
                    if (board_pieces[row - k][col + k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col + k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row - k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else if (col == BOARD_DIM - 1) {
                for (int k = 0; k < BOARD_DIM; k++) {
                    if (board_pieces[row - k][col - k][k].getPlayer() != potentialWinner) {
                        foundWinDiag = false;
                    }
                    if (board_pieces[row][col - k][k].getPlayer() != potentialWinner) {
                        foundWinRow = false;
                    }
                    if (board_pieces[row - k][col][k].getPlayer() != potentialWinner) {
                        foundWinCol = false;
                    }
                }
                if (foundWinCol || foundWinDiag || foundWinRow) {
                    return true;
                }
            } else {
                throw new Error("not a top corner 2");
            }
        } else {
            throw new Error("not a top corner 3");
        }
        return false;
    }

    private boolean isEdge(int i, int j) {
        return ((i == 0 || i == BOARD_DIM - 1) && (j != 0 && j != BOARD_DIM - 1)) ||
                ((j == 0 || j == BOARD_DIM - 1) && (i != 0 && i != BOARD_DIM - 1));
    }

    private boolean isCorner(int i, int j) {
        return ((i == 0 || i == BOARD_DIM - 1) && (j == 0 || j == BOARD_DIM - 1));
    }

    private boolean checkLevel(Player potentialWinner, int row, int col, int k) {
        boolean foundWin = true;
        for (int i = 0; i < BOARD_DIM; i++) {
            if (board_pieces[i][col][k].getPlayer() != potentialWinner) {
                foundWin = false;
            }
        }
        if (foundWin) {
            return foundWin;
        }
        foundWin = true;
        for (int j = 0; j < BOARD_DIM; j++) {
            if (board_pieces[row][j][k].getPlayer() != potentialWinner) {
                foundWin = false;
            }
        }
        if (foundWin) {
            return foundWin;
        }
        foundWin = true;
        if (row + col == BOARD_DIM - 1) { //up diagonal
            for (int d = 0; d < BOARD_DIM; d++) {
                if (board_pieces[BOARD_DIM - 1 - d][d][k].getPlayer() != potentialWinner) {
                    foundWin = false;
                }
            }
        } else if (row - col == 0) {
            for (int d = 0; d < BOARD_DIM; d++) {
                if (board_pieces[d][d][k].getPlayer() != potentialWinner) {
                    foundWin = false;
                }
            }
        } else {
            return false;
        }
        return foundWin;
    }

    private boolean checkColumn(Player potentialWinner, int i, int j) {
        for (int k = 0; k < BOARD_DIM; k++) {
            if (board_pieces[i][j][k].getPlayer() != potentialWinner) {
                return false;
            }
        }
        return true;
    }

    private boolean isBottom(int k) {
        return k == BOARD_DIM - 1;
    }

    private boolean isTop(int k) {
        return k == 0;
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
