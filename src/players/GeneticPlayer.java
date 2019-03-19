package players;

import parts.Board;
import parts.GameState;
import parts.Move;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by liamkreiss on 3/18/19.
 */
public class GeneticPlayer extends ComputerPlayer{
    private static final int WEIGHT_SIZE = 9;
    double[] weights = new double[]{

    };

    public GeneticPlayer(String name, String filename) throws FileNotFoundException {
        super(name);
        Scanner console = new Scanner(new File(filename));
        double[] weights1 = new double[WEIGHT_SIZE];

        Scanner line = new Scanner(console.nextLine());
        for (int i = 0; i < weights1.length; i++) {
            weights1[i] = line.nextDouble();
        }
        this.weights = weights1;
    }

    public GeneticPlayer(String name, double[] weights) {
        super(name);
        this.weights = weights;
    }

    public Move getMove(Board gameboard, Player opponent) {
        Move m;
        Board b;
        double moveScore;
        HashMap<Move, Double> moveScores = new HashMap<>();

        for (int i = 0; i < gameboard.getSize(); i++) {
            for (int j = 0; j < gameboard.getSize(); j++) {
                m = new Move(i, j, this.getPlayersPiece());
                if (gameboard.isLegalMove(m)) {
                    b = gameboard.clone();
                    moveScore = getMoveScore(m, b, opponent);
                    moveScores.put(m, moveScore);
                }
            }
        }

        double biggestValue = -1000000.;
        Move bestMove = null;
        for (Move potentialMove : moveScores.keySet()) {
            if (moveScores.get(potentialMove) > biggestValue) {
                biggestValue = moveScores.get(potentialMove);
                bestMove = potentialMove;
            }
        }
        return bestMove;
    }

    /*Board breakdown

    Level 0:
    ABBA
    BCCB
    BCCB
    ABBA

    Level 1:
    DEED
    EFFE
    EFFE
    DEED

    Level 2:
    DEED
    EFFE
    EFFE
    DEED

    Level 3:
    ABBA
    BCCB
    BCCB
    ABBA

    Weights:
    [A, B, C, D, E, F, three with open fourth, level-safe, opponent-death moves]
     */

    //sums up many different board characteristics
    private double getMoveScore(Move m, Board b, Player opponent) {
        int level = b.playMove(m);
        GameState gs = b.getGameState(m, level);

        //check game over
        if (gs.isGameOver()) {
            if (gs.getWinner() == null) {
                return 0;
            } else if (gs.getWinner().equals(this)) {
                return Double.MAX_VALUE;
            } else {
                throw new Error("not sure how game ended");
            }
        }

        double scoreSum = 0;

        //count piece spots
        for (int i = 0; i < b.getSize(); i++) {
            for (int j = 0; j < b.getSize(); j++) {
                for (int k = 0; k < b.getSize(); k++) {
                    if (b.getPieceSpace(i, j, k).isOccupied()) {
                        if (b.getPieceSpace(i, j, k).getPlayer().equals(this)) {
                            scoreSum += sumPieceSpots(b, i, j, k);
                        }
                    }
                }
            }
        }

        //count three with open fourth
        //TODO

        //count level safe
        for (int i = 0; i < b.getSize(); i++) {
            if (checkLevel(b, i)) {
                scoreSum += weights[7];
            }
        }


        //count opponent death moves
        for (int i = 0; i < b.getSize(); i++) {
            for (int j = 0; j < b.getSize(); j++) {
                Board futureGame = b.clone();
                Move oppMove = new Move(i, j, opponent.getPlayersPiece());
                if (futureGame.isLegalMove(oppMove)) {
                    int k = futureGame.playMove(oppMove);
                    GameState futureGS = futureGame.getGameState(oppMove, k);
                    if (!futureGS.isGameOver()) {
                        Move playerMove = new Move(i, j, this.getPlayersPiece());
                        if (futureGame.isLegalMove(playerMove)) {
                            k = futureGame.playMove(playerMove);
                            futureGS = futureGame.getGameState(playerMove, k);
                            if (futureGS.isGameOver()) {
                                scoreSum += weights[8];
                            }
                        }
                    } else {
                        return Double.MIN_VALUE;
                    }
                }
            }
        }

        return scoreSum;
    }

    //checks if the player has covered every possible win on the level
    private boolean checkLevel(Board b, int level) {
        //check rows
        boolean levelSafe = true, foundPiece;
        for (int i = 0; i < b.getSize(); i++) {
            foundPiece = false;
            for (int j = 0; j < b.getSize(); j++) {
                if (b.getPieceSpace(i, j, level).isOccupied()) {
                    if (b.getPieceSpace(i, j, level).getPlayer().equals(this)) {
                        foundPiece = true;
                    }
                }
            }
            if (!foundPiece) {
                levelSafe = false;
            }
        }

        //check cols
        for (int j = 0; j < b.getSize(); j++) {
            foundPiece = false;
            for (int i = 0; i < b.getSize(); i++) {
                if (b.getPieceSpace(i, j, level).isOccupied()) {
                    if (b.getPieceSpace(i, j, level).getPlayer().equals(this)) {
                        foundPiece = true;
                    }
                }
            }
            if (!foundPiece) {
                levelSafe = false;
            }
        }

        //check diags
        foundPiece = false;
        for (int i = 0; i < b.getSize(); i++) {
            if (b.getPieceSpace(i, i, level).isOccupied()) {
                if (b.getPieceSpace(i, i, level).getPlayer().equals(this)) {
                    foundPiece = true;
                }
            }
            if (!foundPiece) {
                levelSafe = false;
            }
        }

        foundPiece = false;
        for (int i = 0; i < b.getSize(); i++) {
            if (b.getPieceSpace(b.getSize() - 1 - i, i, level).isOccupied()) {
                if (b.getPieceSpace(b.getSize() - 1 - i, i, level).getPlayer().equals(this)) {
                    foundPiece = true;
                }
            }
            if (!foundPiece) {
                levelSafe = false;
            }
        }
        return levelSafe;
    }

    private double sumPieceSpots(Board b, int row, int col, int k) {
        double scoreSum = 0;
        if (row == 0 || row == b.getSize() - 1) {
            if (col == 0 || col == b.getSize() - 1) {
                if (k == 0 || k == b.getSize() - 1) {
                    //A
                    scoreSum += weights[0];
                } else {
                    //D
                    scoreSum += weights[3];
                }
            } else {
                if (k == 0 || k == b.getSize() - 1) {
                    //B
                    scoreSum += weights[1];
                } else {
                    //E
                    scoreSum += weights[4];
                }
            }
        } else {
            if (col == 0 || col == b.getSize() - 1) {
                if (k == 0 || k == b.getSize() - 1) {
                    //B
                    scoreSum += weights[1];
                } else {
                    //E
                    scoreSum += weights[4];
                }
            } else {
                if (k == 0 || k == b.getSize() - 1) {
                    //C
                    scoreSum += weights[2];
                } else {
                    //F
                    scoreSum += weights[5];
                }
            }
        }
        return scoreSum;
    }

    public double[] getWeights() {
        return this.weights;
    }
}
