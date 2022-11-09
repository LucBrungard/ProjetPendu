package game.modes;

import game.Game;
import game.GameSetting;

public class Golden extends Game {
    public Golden(GameSetting settings) {
        super(settings);
    }

    private static String voyelle = "aeiouy";

    @Override
    public void guessLetter(char character) {}

    @Override
    public void startSpecialRule() {
        this.maxErrors = 1;
        
        // Show all voyelles
        int idx;
        StringBuilder tmp = new StringBuilder(this.currentStateWord);
        for (String str : voyelle.split("")) {
            idx = this.toFind.indexOf(str);
            while (idx != -1) {
                tmp.replace(2*idx, 2*idx + 1, str);
                idx = this.toFind.indexOf(str, idx + 1);
            }
        }

        this.currentStateWord = tmp.toString();
    }

    @Override
    public String getDescription() {
        String res = "Le mode de jeu Essai en Or est un mode dans lequel toutes les voyelles d'un mot sont révélées.";
        res = res.concat("\nCependant, vous n'avez qu'un seul essai pour proposer le mot à trouver.");
        res = res.concat("\nRefléchissez bien avant de jouer !");

        return res;
    }
}
