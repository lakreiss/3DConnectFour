package players;

/**
 * Created by liamkreiss on 4/1/19.
 */
public enum Difficulty {

    HUMAN("Human", 0), EASY("Easy\nComputer", 1), MEDIUM("Medium\nComputer", 2), HARD("Hard\nComputer", 3);

    private String difficulty;
    private int number;

    Difficulty(String type, int number) {
        this.difficulty = type;
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return difficulty;
    }
}
