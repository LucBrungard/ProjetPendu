package game;

import java.util.HashSet;
import java.util.Set;

import game.settings.Difficulty;
import game.settings.WordsDictionnary;

public class Game {
    private Difficulty difficulty = Difficulty.NORMAL;
    private String toFind;
    private String currentStateWord = "";
    //private int maxErrors;
    private int errors = 0;
    private GameState gameState;
    private Set<String> alreadyProposed = new HashSet<String>();

    public Game() {
        //this.setMaxErrors(difficulty);
    }

    public void start() {
        this.errors = 0;
        this.currentStateWord = "";
        this.gameState = GameState.IN_GAME;
        this.alreadyProposed.clear();
        this.toFind = WordsDictionnary.getRandomWord(this.difficulty);
        for (int i = 0; i < toFind.length(); i++) {
            currentStateWord = currentStateWord.concat("_ ");
        }
    }

    /*
    private void setMaxErrors(Difficulty difficulty) {
        if (difficulty.equals(Difficulty.EASY)) {
            this.maxErrors = 15;
        }
        else if (difficulty.equals(Difficulty.NORMAL)) {
            this.maxErrors = 10;
        }
        else if (difficulty.equals(Difficulty.HARD)) {
            this.maxErrors = 5;
        }
    }
    */

    public boolean guessLetter(char character) {
        String charAsString = Character.toString(Character.toLowerCase(character));
        this.alreadyProposed.add(charAsString);
        int idx = this.toFind.indexOf(charAsString);
        StringBuilder tmp = new StringBuilder(this.currentStateWord);

        if (idx != -1) {
            do {
                tmp.replace(2*idx, 2*idx + 1, charAsString);
                idx = this.toFind.indexOf(charAsString, idx + 1);
            } while (idx != -1);

            this.currentStateWord = tmp.toString();

            return true;
        }

        this.errors += 1;
        return false;
    }

    public void setState(GameState gameState) {
        if (gameState != null) {
            this.gameState = gameState;
        }
    }

    public GameState getState() {
        return this.gameState;
    }

    public Set<String> getAlreadyProposed() {
        return this.alreadyProposed;
    }

    public boolean hasBeenFound() {
        return this.currentStateWord.replaceAll(" ", "").equals(this.toFind);
    }

    public boolean hasLost() {
        return this.errors == this.difficulty.maxErrors;
    }

    public boolean guessWordToFind(String word) {
        boolean res = this.toFind.equalsIgnoreCase(word);
        if (!res) this.errors += 1;

        return res;
    }

    public String getCurrentStateWord() {
        return this.currentStateWord;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        //this.setMaxErrors(difficulty);
    }

    public int getErrors() {
        return this.errors;
    }

    public int getMaxErrors() {
        return this.difficulty.maxErrors;
    }

    public String getToFind() {
        return this.toFind;
    }

    @Override
    public String toString() {
        return "Game [difficulty=" + difficulty + ", toFind=" + toFind + ", currentStateWord=" + currentStateWord
                + ", maxErrors=" + difficulty.maxErrors + ", errors=" + errors + ", gameState=" + gameState + ", alreadyProposed="
                + alreadyProposed + "]";
    }
}
