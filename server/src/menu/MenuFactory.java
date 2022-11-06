package menu;

public final class MenuFactory {
    private MenuFactory() {};

    private static Menu StartMenu;
    private static Menu ParametersMenu;
    private static Menu DifficultyMenu;

    public static Menu getStartMenu() {
        if (StartMenu == null) {
            Option start = new Option(1, "Commencer une nouvelle partie");
            Option parameters = new Option(2, "Paramètres");
            Option quit = new Option(3, "Quitter");

            StartMenu = new Menu(MenuName.START_MENU, new Option[]{ start, parameters, quit });
            
            parameters.addNext(getParametersMenu());
        }

        return StartMenu;
    }

    private static Menu getParametersMenu() {
        if (ParametersMenu == null) {
            Option difficulty = new Option(1, "Difficulté");
            Option back = new Option(2, "Retour");
            
            ParametersMenu = new Menu(MenuName.PARAMETERS_MENU, new Option[]{ difficulty, back });
            
            difficulty.addNext(getDifficultyMenu());
            back.addPrevious(getStartMenu());
        }

        return ParametersMenu;
    }

    private static Menu getDifficultyMenu() {
        if (DifficultyMenu == null) {
            Option easy = new Option(1, "Facile");
            Option normal = new Option(2, "Normal");
            Option hard = new Option(3, "Difficile");
            Option back = new Option(4, "Retour");

            DifficultyMenu = new Menu(MenuName.DIFFICULTY_MENU, new Option[]{ easy, normal, hard, back });

            back.addPrevious(getParametersMenu());
        }

        return DifficultyMenu;
    }
}
