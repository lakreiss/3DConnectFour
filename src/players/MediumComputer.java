package players;

import parts.Board;
import parts.GameState;
import parts.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by liamkreiss on 3/13/19.
 */
public class MediumComputer extends ComputerPlayer {

    public MediumComputer() {
        super();
    }

    public MediumComputer(String name) {
        super(name);
    }

    public Move getMove(Board gameboard, Player opponent) {
        HashMap<Move, Double> moveScores = lookAheadPlayer(gameboard, opponent, 2);

        Move bestMove = null;
        double bestScore = -1000;
        for (Move potentialMove : moveScores.keySet()) {
            if (moveScores.get(potentialMove) > bestScore) {
                bestMove = potentialMove;
                bestScore = moveScores.get(potentialMove);
            }
        }
        return bestMove;

//        return gameboard.isLegalMove(m) ? m : getMove(gameboard);
    }
    private HashMap<Move, Double> lookAheadPlayer(Board possibleGameboard, Player opponent, int numMovesToLookAhead) {
        Move m = null;
        Board b;
        int k; //level
        GameState gs;
        HashMap<Move, Double> moveScores = new HashMap<>();
        HashMap<Move, Double> nextMoveScores;
        for (int i = 0; i < possibleGameboard.getSize(); i++) {
            for (int j = 0; j < possibleGameboard.getSize(); j++) {
                m = new Move(i, j, this.getPlayersPiece());
                if (possibleGameboard.isLegalMove(m)) {
                    b = possibleGameboard.clone();
                    k = b.playMove(m);
                    gs = b.getGameState(m, k);
                    if (gs.isGameOver()) {
                        if (gs.getWinner().equals(null)) {
                            //tie
                            moveScores.put(m, 0.);
                        } else if (gs.getWinner().equals(opponent)) {
                            //lost
                            moveScores.put(m, -1.);
                        } else {
                            //won
                            moveScores.put(m, 1.);
                        }
                    } else {
                        if (numMovesToLookAhead < 2) {
                            moveScores.put(m, 0.5);
                        } else {
                            nextMoveScores = lookAheadOpponent(b, opponent, numMovesToLookAhead - 1);
                            double biggestValue = -1000.;
                            for (Move potentialMove : nextMoveScores.keySet()) {
                                if (nextMoveScores.get(potentialMove) > biggestValue) {
                                    biggestValue = nextMoveScores.get(potentialMove);
                                }
                                moveScores.put(m, biggestValue);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(moveScores);
        return moveScores;
    }

    private HashMap<Move, Double> lookAheadOpponent(Board possibleGameboard, Player opponent, int numMovesToLookAhead) {
        //currently opponents turn
        Move m = null;
        Board b;
        int k; //level
        GameState gs;
        HashMap<Move, Double> moveScores = new HashMap<>();
        HashMap<Move, Double> nextMoveScores;
        for (int i = 0; i < possibleGameboard.getSize(); i++) {
            for (int j = 0; j < possibleGameboard.getSize(); j++) {
                m = new Move(i, j, opponent.getPlayersPiece());
                if (possibleGameboard.isLegalMove(m)) {
                    b = possibleGameboard.clone();
                    k = b.playMove(m);
                    gs = b.getGameState(m, k);
                    if (gs.isGameOver()) {
                        if (gs.getWinner().equals(null)) {
                            //tie
                            moveScores.put(m, 0.);
                        } else if (gs.getWinner().equals(opponent)) {
                            //lost
                            moveScores.put(m, -1.);
                        } else {
                            //won
                            moveScores.put(m, 1.);
                        }
                    } else {
                        nextMoveScores = lookAheadPlayer(b, opponent, numMovesToLookAhead);
                        double smallestValue = 1000.;
                        for (Move potentialMove : nextMoveScores.keySet()) {
                            if (nextMoveScores.get(potentialMove) < smallestValue) {
                                smallestValue = nextMoveScores.get(potentialMove);
                            }
                            moveScores.put(m, smallestValue);
                        }
                    }
                }
            }
        }
        System.out.println(moveScores);
        return moveScores;
    }
}
