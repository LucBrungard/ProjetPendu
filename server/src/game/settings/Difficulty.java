package game.settings;

import java.util.List;

public enum Difficulty {
    BEGINNER("debutant", 15, WordsDictionnary.words7To15Letters, 
        "Des mots de 7 à 15 lettres à trouver"),
    EASY("facile", 15, WordsDictionnary.words4To8Letters,
        "Des mots de 4 à 8 lettres à trouver"),
    AMATEUR("amateur", 10, WordsDictionnary.words7To15Letters,
        "Des mots de 7 à 15 lettres à trouver"),
    NORMAL("normal", 10, WordsDictionnary.words4To8Letters,
            "Des mots de 4 à 8 lettres à trouver"),
    HARD("difficile", 5, WordsDictionnary.words5To9Letters,
            "Des mots de 5 à 9 lettres à trouver"),
    HARDCORE("hardcore", 5, WordsDictionnary.words2To5Letters,
            "Des mots de 2 à 5 lettres à trouver");

    public final String value;
    public final int maxErrors;
    public final List<String> words;
    public final String description;

    private Difficulty(String value, int maxErrors, List<String> words, String description) {
        this.value = value;
        this.maxErrors = maxErrors;
        this.words = words;
        this.description = description + ". Jusqu'à " + maxErrors + " erreurs possibles";
    }

    public static Difficulty fromString(String str) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.value.equalsIgnoreCase(str)) return difficulty;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.value.substring(0, 1).toUpperCase() + this.value.substring(1) + " (" + this.description + ")";
    }
}
