package parts;

import players.Player;

/**
 * Created by liamkreiss on 3/9/19.
 */
public class Piece {
    private Player owner;
//    Color c; TODO

    public Piece(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }
}
