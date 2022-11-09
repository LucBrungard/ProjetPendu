package menu;

import java.io.BufferedReader;
import java.io.PrintStream;

import game.ClientState;
import game.GameSetting;
import game.modes.Classic;
import game.modes.Golden;
import game.settings.Difficulty;
import game.GameHandler;

public class Menu {
    private MenuName name;
    private Option[] options;

    public Menu(MenuName name, Option[] options) {
        this.name = name;
        if (options.length == 0 ) return;

        this.options = options;
    }

    public void showMenu(BufferedReader inStream, PrintStream outStream, GameSetting settings) {
        for (Option option : options) {
            outStream.print(option);

            if (this.name.equals(MenuName.DIFFICULTY_MENU)) {
                if (settings.getDifficulty().value.equalsIgnoreCase(option.getOption())) {
                    outStream.print(" \u2705");
                }
            }
            outStream.println();
        }
        outStream.println("Que choisissez vous ?");
    }

    public Menu handleChoice(String choice, GameHandler gameHandler) {
        gameHandler.setClientState(ClientState.IN_MENU_PROGRESS);
        try {
            int choiceInt = Integer.parseInt(choice);
            for (Option option : options) {
                if (option.isValid(choiceInt)) {
                    if (option.getNext() != null) { // Recursive case next
                        return option.getNext();
                    } else if (option.getPrevious() != null) { // Recursive case previous
                        return option.getPrevious();
                    } else { // Leafs
                        if (this.name.equals(MenuName.DIFFICULTY_MENU)) { // Special case difficulty
                            gameHandler.getSettings().setDifficulty(Difficulty.fromString(option.getOption()));
                            gameHandler.getOutStream().println("La difficulté a bien été modifiée");
                            return this;
                        } else if (this.name.equals(MenuName.START_MENU)) {
                            // Special case "quit"
                            if (option.getOption().equalsIgnoreCase("quitter")) {
                                gameHandler.setClientState(ClientState.QUIT);;
                                return null;
                            }
                        } else if (this.name.equals(MenuName.GAMES_MODE_MENU)) {
                            if (option.getOption().equalsIgnoreCase("Classique")) {
                                gameHandler.setGame(new Classic(gameHandler.getSettings()));
                                return null;
                            }
                            if (option.getOption().equalsIgnoreCase("Essai en or")) {
                                gameHandler.setGame(new Golden(gameHandler.getSettings()));
                                return null;
                            }
                        } else if (this.name.equals(MenuName.END_MENU)) {
                            if (option.getOption().equalsIgnoreCase("rejouer")) {
                                gameHandler.setClientState(ClientState.IN_GAME);
                                return null;
                            }

                            // Special case "quit"
                            if (option.getOption().equalsIgnoreCase("quitter")) {
                                gameHandler.setClientState(ClientState.QUIT);
                                return null;
                            }
                        }
                    }
                }
            }

            // No options has been validated
            gameHandler.setClientState(ClientState.IN_MENU_ERROR);

            return this;

        } catch (NumberFormatException e) {
            gameHandler.setClientState(ClientState.IN_MENU_ERROR);
            return this;
        }
    }

    public MenuName getName() {
        return this.name;
    }

    @Override
    public String toString() {
        String str = "Menu [name=" + name + ", options=[";
        for (Option option : this.options) {
            str = str.concat("\n\t" + option.toString());
        }
        str = str.concat("]");

        return str;
    }
}
