package game.settings;

import java.util.List;

public enum Difficulty {
    BEGINNER("debutant", 15, WordsDictionnary.words7To15Letters),
    EASY("facile", 15, WordsDictionnary.words4To8Letters),
    AMATEUR("amateur", 10, WordsDictionnary.words7To15Letters),
    NORMAL("normal", 10, WordsDictionnary.words4To8Letters),
    HARD("difficile", 5, WordsDictionnary.words5To9Letters),
    HARDCORE("hardcore", 5, WordsDictionnary.words2To5Letters);

    public final String value;
    public final int maxErrors;
    public final List<String> words;

    private Difficulty(String value, int maxErrors, List<String> words) {
        this.value = value;
        this.maxErrors = maxErrors;
        this.words = words;
    }

    public static Difficulty fromString(String str) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.value.equalsIgnoreCase(str)) return difficulty;
        }
        return null;
    }
}
