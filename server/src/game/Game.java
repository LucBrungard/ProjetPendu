package game;

import game.settings.Difficulty;
import game.settings.WordsDictionnary;

public class Game {
    private Difficulty difficulty;
    private String toFind;
    private String currentStateWord = "";
    private int maxErrors;
    private int errors = 0;

    public Game(Difficulty difficulty) {
        this.toFind = WordsDictionnary.getRandomWord();
        for (int i = 0; i < toFind.length(); i++) {
            currentStateWord.concat("_ ");
        }
        this.setMaxErrors(difficulty);
        this.difficulty = difficulty;
    }

    private void setMaxErrors(Difficulty difficulty) {
        System.out.println(Difficulty.EASY.value);
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

    public boolean guessLetter(char character) {
        String charAsString = Character.toString(character);
        int idx = this.toFind.indexOf(charAsString);
        StringBuilder tmp = new StringBuilder(this.currentStateWord);

        if (idx != -1) {
            do {
                tmp.replace(2*idx, 2*idx + 1, charAsString);
                idx = this.toFind.indexOf(charAsString, idx + 1);
            } while (idx != -1);

            this.currentStateWord = tmp.toString();

            return true;
        } else {
            this.errors += 1;
            return false;
        }
    }

    public boolean hasLost() {
        return this.errors == this.maxErrors;
    }

    public boolean guessWordToFind(String word) {
        return this.toFind.equals(word);
    }

    public String getCurrentStateWord() {
        return this.currentStateWord;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
