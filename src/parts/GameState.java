package parts;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class GameState {
    boolean gameOver;
    boolean tie;
    Player winner;

    public GameState(boolean gameOver, Player winner) {
        this.gameOver = gameOver;
        this.winner = winner;
        this.tie = false;
        if (gameOver && winner.equals(null)) {
            this.tie = true;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isTie() {
        return tie;
    }

    public Player getWinner() {
        return winner;
    }
}
