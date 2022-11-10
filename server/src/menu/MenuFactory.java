package menu;

import game.GameUtil;
import game.settings.Difficulty;

public final class MenuFactory {
    private MenuFactory() {};

    private static Menu StartMenu;
    private static Menu ParametersMenu;
    private static Menu DifficultyMenu;
    private static Menu EndMenu;
    private static Menu GameModesMenu;
    private static Menu LeaderBoardMenu;

    public static Menu getStartMenu() {
        if (StartMenu == null) {
            Option start = new Option(1, "Jouer");
            Option leaderboard = new Option(2, "Classement");
            Option parameters = new Option(3, "Paramètres");
            Option quit = new Option(4, "Quitter");

            StartMenu = new Menu(MenuName.START_MENU, new Option[]{ start, leaderboard, parameters, quit });
            
            start.addNext(getGameModesMenu());
            leaderboard.addNext(getLeaderBoardMenu());
            parameters.addNext(getParametersMenu());
        }

        return StartMenu;
    }

    private static Menu getGameModesMenu() {
        if (GameModesMenu == null) {
            Option classic = new Option(1, "Classique");
            Option golden = new Option(2, "Essai en or");
            Option back = new Option(3, "Retour");

            GameModesMenu = new Menu(MenuName.GAMES_MODE_MENU, new Option[]{ classic, golden, back });

            back.addNext(getStartMenu());
        }

        return GameModesMenu;
    }

    static Menu getParametersMenu() {
        if (ParametersMenu == null) {
            Option difficulty = new Option(1, "Difficulté");
            Option back = new Option(2, "Retour");
            
            ParametersMenu = new Menu(MenuName.PARAMETERS_MENU, new Option[]{ difficulty, back });
            
            difficulty.addNext(getDifficultyMenu());
            back.addNext(getStartMenu());
        }

        return ParametersMenu;
    }

    private static Menu getDifficultyMenu() {
        if (DifficultyMenu == null) {
            Option beginner = new Option(1, Difficulty.BEGINNER.toString());
            Option easy = new Option(2, Difficulty.EASY.toString());
            Option amateur = new Option(3, Difficulty.AMATEUR.toString());
            Option normal = new Option(4, Difficulty.NORMAL.toString());
            Option hard = new Option(5, Difficulty.HARD.toString());
            Option hardcore = new Option(6, Difficulty.HARDCORE.toString());
            Option back = new Option(7, "Retour");

            DifficultyMenu = new Menu(MenuName.DIFFICULTY_MENU, new Option[]{ beginner, easy, amateur, normal, hard, hardcore, back });

            back.addNext(getParametersMenu());
        }

        return DifficultyMenu;
    }

    public static Menu getLeaderBoardMenu() {
        if (LeaderBoardMenu == null) {
            Option beginner = new Option(1, GameUtil.captitalize(Difficulty.EASY.value));
            Option easy = new Option(2, GameUtil.captitalize(Difficulty.EASY.value));
            Option amateur = new Option(3, GameUtil.captitalize(Difficulty.AMATEUR.value));
            Option normal = new Option(4, GameUtil.captitalize(Difficulty.NORMAL.value));
            Option hard = new Option(5, GameUtil.captitalize(Difficulty.HARD.value));
            Option hardcore = new Option(6, GameUtil.captitalize(Difficulty.HARDCORE.value));
            Option back = new Option(7, "Retour");

            LeaderBoardMenu = new Menu(MenuName.LEADERBOARD_MENU, new Option[]{ beginner, easy, amateur, normal, hard, hardcore, back });

            back.addNext(getStartMenu());
        }

        return LeaderBoardMenu;
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
