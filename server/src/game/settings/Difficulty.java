package game.settings;

public enum Difficulty {
    EASY("facile"),
    NORMAL("normal"),
    HARD("difficile");

    public final String value;

    private Difficulty(String value) {
        this.value = value;
    }

    public static Difficulty fromString(String str) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.value.equalsIgnoreCase(str)) return difficulty;
        }
        return null;
    }
}
