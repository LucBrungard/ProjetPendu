package menu;

public final class MenuFactory {
    private MenuFactory() {};

    private static Menu StartMenu;
    private static Menu ParametersMenu;
    private static Menu DifficultyMenu;
    private static Menu EndMenu;

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
            Option beginner = new Option(1, "Debutant");
            Option easy = new Option(2, "Facile");
            Option amateur = new Option(3, "Amateur");
            Option normal = new Option(4, "Normal");
            Option hard = new Option(5, "Difficile");
            Option hardcore = new Option(6, "Hardcore");
            Option back = new Option(7, "Retour");

            DifficultyMenu = new Menu(MenuName.DIFFICULTY_MENU, new Option[]{ beginner, easy, amateur, normal, hard, hardcore, back });

            back.addPrevious(getParametersMenu());
        }

        return DifficultyMenu;
    }

    public static Menu getEndMenu() {
        if (EndMenu == null) {
            Option startMenu = new Option(1, "Retourner au menu principal");
            Option replay = new Option(2, "Rejouer");
            Option quit = new Option(3, "Quitter");

            EndMenu = new Menu(MenuName.END_MENU, new Option[]{ startMenu, replay, quit });

            startMenu.addNext(StartMenu);
        }

        return EndMenu;
    }
}
