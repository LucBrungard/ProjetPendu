package menu;

public enum MenuName {
   START_MENU("start-menu"), //
   HELP_MENU("help-menu"), //
   HELP_MODES_MENU("help-modes-menu"), //
   GAMES_MODE_MENU("games-mode-menu"), //
   PARAMETERS_MENU("parameters-menu"), //
   DIFFICULTY_MENU("difficulty-menu"), //
   LEADERBOARD_MENU("leaderboard-menu"), //
   END_MENU("end-menu");

   public final String name;

   private MenuName(String name) {
      this.name = name;
   }
}
