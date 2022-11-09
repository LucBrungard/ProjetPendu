package menu;

public enum MenuName {
    START_MENU("start-menu"),
    GAMES_MODE_MENU("games-mode-menu"),
    PARAMETERS_MENU("parameters-menu"),
    DIFFICULTY_MENU("difficulty-menu"),
    END_MENU("end-menu");

    public final String name;

    private MenuName(String name) {
        this.name = name;
    }
}
