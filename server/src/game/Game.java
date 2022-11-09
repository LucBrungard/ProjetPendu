package game;

import java.util.HashSet;
import java.util.Set;

import game.settings.WordsDictionnary;

public abstract class Game {
    /** The word to find */
    protected String toFind;

    /** The current state of the guessing of the client */
    protected String currentStateWord;

    /** The number of errors made by the client */
    protected int errors = 0;

    /** THe max errors a client can make */
    protected int maxErrors;

    /** The state of the game. See {@link game.GameState} */
    protected GameState gameState;

    /** The letters already proposed by the client */
    protected Set<String> alreadyProposed = new HashSet<String>();

    /** The settings of a game */
    protected GameSetting settings;

    public Game(GameSetting settings) {
        if (settings != null) this.settings = settings;
    }

    
    /**
     * Initialize the variables to start a new game
     */
    public void start() {
        this.errors = 0;
        this.maxErrors = this.settings.getDifficulty().maxErrors;

        this.toFind = WordsDictionnary.getRandomWord(this.settings.getDifficulty());
        this.currentStateWord = "";
        for (int i = 0; i < toFind.length(); i++) {
            currentStateWord = currentStateWord.concat("_ ");
        }

        this.alreadyProposed.clear();

        this.gameState = GameState.RUNNING;

        this.startSpecialRule();
    }


    /** Add special cases when playing modes */
    public abstract void startSpecialRule();


    /**
     * Check if a letter has already been proposed
     * @param character The letter to check
     * @return True if not already proposed, false otherwise
     */
    public boolean validLetter(char character) {
        String charAsString = Character.toString(Character.toLowerCase(character));
        return this.alreadyProposed.add(charAsString);
    }


    /**
     * Check if a letter is in the word to find and update the currentStateWord
     * @param character The letter to check
     */
    public void guessLetter(char character) {
        String charAsString = Character.toString(Character.toLowerCase(character));

        int idx = this.toFind.indexOf(charAsString);
        StringBuilder tmp = new StringBuilder(this.currentStateWord);

        if (idx == -1) {
            this.errors += 1;
            if (this.hasLost()) this.gameState = GameState.LOST;
            return;
        }

        do {
            tmp.replace(2*idx, 2*idx + 1, charAsString);
            idx = this.toFind.indexOf(charAsString, idx + 1);
        } while (idx != -1);

        this.currentStateWord = tmp.toString();

        if (this.hasBeenFound()) this.gameState = GameState.WON;
    }


    /**
     * Check if the word to find is the word proposed
     * @param word The word to check
     * @return True if the word is good, false otherwise
     */
    public void guessWordToFind(String word) {
        boolean res = this.toFind.equalsIgnoreCase(word);
        if (!res) {
            this.errors += 1;
        } else {
            this.gameState = GameState.WON;
        }
        if (this.hasLost()) this.gameState = GameState.LOST;
    }


    /**
     * Check if the word has been found
     * @return True if it has been found, false otherwise
     */
    private boolean hasBeenFound() {
        return this.currentStateWord.replaceAll(" ", "").equals(this.toFind);
    }

    
    /**
     * Check if the game is lost
     * @return True if the game is lost, false otherwise
     */
    private boolean hasLost() {
        return this.errors == this.maxErrors;
    }

    ////////////////////////////////////////////////////////////////////
    //////////////////             GETTERS             /////////////////
    ////////////////////////////////////////////////////////////////////
    public GameState getState() {
        return this.gameState;
    }

    public Set<String> getAlreadyProposed() {
        return this.alreadyProposed;
    }

    public String getCurrentStateWord() {
        return this.currentStateWord;
    }

    public static String getForfeitCommand() {
        return "/ff";
    }

    public int getErrors() {
        return this.errors;
    }
    
    public int getMaxErrors() {
        return this.maxErrors;
    }
    
    public String getToFind() {
        return this.toFind;
    }

    /** Returns the description of this mode */
    public abstract String getDescription();
    
    ////////////////////////////////////////////////////////////////////
    //////////////////             SETTERS             /////////////////
    ////////////////////////////////////////////////////////////////////
    public void setState(GameState gameState) {
        if (gameState != null) {
            this.gameState = gameState;
        }
    }

    public void setMaxErrors(int value) {
        this.maxErrors = value;
    }


    @Override
    public String toString() {
        return "Game [toFind=" + toFind + ", currentStateWord=" + currentStateWord + ", errors=" + errors
                + ", maxErrors=" + maxErrors + ", gameState=" + gameState + ", alreadyProposed=" + alreadyProposed
                + ", settings=" + settings + "]";
    }
}
