package game.modes;

import game.Game;
import game.GameSetting;
import game.GameState;

public class PointsRush extends Game {
    protected int points;
    protected int coup;

    public PointsRush(GameSetting settings) {
        super(settings);
    }

    @Override
    public void startSpecialRule() {
        this.mode = "rush";
        this.points = 0;
        this.coup = 1;

    }

    @Override
    public String msgSpecialRule() {
        String res = "Votre score est de : " + this.points;
        return res;
    }

    @Override
    public int getScore() {
        return this.points;
    }

    /**
     * Check if a letter is in the word to find and update the currentStateWord
     * 
     * @param character The letter to check
     */
    @Override
    public void guessLetter(char character) {
        String charAsString = Character.toString(Character.toLowerCase(character));

        int idx = this.toFind.indexOf(charAsString);
        StringBuilder tmp = new StringBuilder(this.currentStateWord);

        if (idx == -1) {
            this.errors += 1;
            this.points -= (10 * this.errors);
            if (this.hasLost())
                this.gameState = GameState.LOST;
            this.points -= 50;
            return;
        }

        int nbLettresTrouvees = 0;

        do {
            tmp.replace(2 * idx, 2 * idx + 1, charAsString);
            idx = this.toFind.indexOf(charAsString, idx + 1);
            nbLettresTrouvees++;
        } while (idx != -1);

        this.points += ((11 - this.coup) * nbLettresTrouvees);

        this.currentStateWord = tmp.toString();

        if (this.hasBeenFound()) {
            this.points += (10 * (11 - this.coup));
            this.gameState = GameState.WON;
        }
    }

    /**
     * Check if the word to find is the word proposed
     * 
     * @param word The word to check
     * @return True if the word is good, false otherwise
     */
    @Override
    public void guessWordToFind(String word) {
        boolean res = this.toFind.equalsIgnoreCase(word);
        if (!res) {
            this.errors += 1;
            this.points -= (10 * this.errors);
            this.coup++;
        } else {
            this.gameState = GameState.WON;
            this.points += (10 * (11 - this.coup));
        }
        if (this.hasLost()) {
            this.gameState = GameState.LOST;
            this.points -= 50;
        }
    }

    public static String getFullDescription() {
        String res = "===================================\n";
        res = res.concat("========== RUEE AUX POINTS ============\n");
        res = res.concat("===================================\n\n");

        res = res.concat("Limite de temps : aucune\n");
        res = res.concat("Nombre d'essais : En fonction de la difficulté\n\n");

        res = res.concat(
                "Détails : Le mode de jeu Ruée aux Points est un mode dans lequel plus vous trouvez le mot rapidement, plus vous gagnerez de points..\n");
        res = res.concat(
                "Plus vous mettez de temps à trouver des lettres et à trouvez le mot, moins vous gagnerez de points.\n");
        res = res.concat("De plus, les erreurs vous coûtent des points.\n");
        res = res.concat(
                "En fin de partie, vous pourrez, si vous le souhaitez, enregistrer votre score et pourquoi pas, rejoindre le top du Leaderbord...\n");
        res = res.concat("Refléchissez bien avant de jouer !\n");
        return res;
    }

}
