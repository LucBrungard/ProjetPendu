package menu;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

import game.Game;
import game.GameState;
import game.settings.Difficulty;

public class Menu {
    private MenuName name;
    private Option[] options;

    public Menu(MenuName name, Option[] options) {
        this.name = name;
        if (options.length == 0 ) return;

        this.options = options;
    }

    public void showMenu(BufferedReader inStream, PrintStream outStream, Game game) {
        game.setState(GameState.IN_MENU_PROGRESS);

        for (Option option : options) {
            outStream.print(option);

            if (this.name.equals(MenuName.DIFFICULTY_MENU)) {
                if (game.getDifficulty().value.equalsIgnoreCase(option.getOption())) {
                    outStream.print(" \u2705");
                }
            }
            outStream.println();
        }
        outStream.println("Que choisissez vous ?");
    }

    public Menu handleChoice(String choice, BufferedReader inStream, PrintStream outStream, Game game) {
        try {
            int choiceInt = Integer.parseInt(choice);
            for (Option option : options) {
                if (option.isValid(choiceInt, inStream, outStream, game)) {
                    if (option.getNext() != null) { // Recursive case next
                        return option.getNext();
                        // option.getNext().showMenu(inStream, outStream, game);
                    } else if (option.getPrevious() != null) { // Recursive case previous
                        return option.getPrevious();
                        // option.getPrevious().showMenu(inStream, outStream, game);
                    } else { // Leafs
                        if (this.name.equals(MenuName.DIFFICULTY_MENU)) { // Special case difficulty
                            game.setDifficulty(Difficulty.fromString(option.getOption()));
                            outStream.println("La difficulté a bien été modifiée");
                            return this;
                            // this.showMenu(inStream, outStream, game);
                        }

                        if (this.name.equals(MenuName.START_MENU)) {
                            // Special case "Play"
                            if (option.getOption().equalsIgnoreCase("Commencer une nouvelle partie")) {
                                // this.startGame(inStream, outStream, game);
                                // return GameState.IN_GAME;
                                game.setState(GameState.IN_GAME);
                                return null;
                            }
    
                            // Special case "quit"
                            if (option.getOption().equalsIgnoreCase("quitter")) {
                                // outStream.println("Aurevoir...");
                                // return GameState.STOP;
                                game.setState(GameState.STOP);
                                return null;
                            }
                        }

                        if (this.name.equals(MenuName.END_MENU)) {
                            if (option.getOption().equalsIgnoreCase("rejouer")) {
                                game.setState(GameState.IN_GAME);
                                return null;
                            }

                            // Special case "quit"
                            if (option.getOption().equalsIgnoreCase("quitter")) {
                                // outStream.println("Aurevoir...");
                                // return GameState.STOP;
                                game.setState(GameState.STOP);
                                return null;
                            }
                        }
                    }
                }
            }

            // No options has been validated
            game.setState(GameState.IN_MENU_ERROR);

            return this;

        } catch (NumberFormatException e) {
            return this;
        }
    }

    public MenuName getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Menu [name=" + name + ", options=" + Arrays.toString(options) + "]";
    }
}
