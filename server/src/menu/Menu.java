package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import game.Game;
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
        boolean isValid = true;
        do {
            this.showOptions(outStream, game);

            try {
                String choice = inStream.readLine();
                if (choice == null) return;

                isValid = this.handleChoice(choice, inStream, outStream, game);
                if (!isValid) {
                    outStream.println("Le choix proposé n'est pas correct");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!isValid);
    }

    private void showOptions(PrintStream outStream, Game game) {
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

    private boolean handleChoice(String choice, BufferedReader inStream, PrintStream outStream, Game game) {
        try {
            int choiceInt = Integer.parseInt(choice);
            for (Option option : options) {
                if (option.isValid(choiceInt, inStream, outStream, game)) {
                    if (option.getNext() != null) { // Recursive case next
                        option.getNext().showMenu(inStream, outStream, game);
                    } else if (option.getPrevious() != null) { // Recursive case previous
                        option.getPrevious().showMenu(inStream, outStream, game);
                    } else { // Leaf of menu
                        // Special case "quit"
                        if (option.getOption().equalsIgnoreCase("quitter")) {
                            outStream.println("Aurevoir...");
                        }

                        // Special case difficulty
                        if (this.name.equals(MenuName.DIFFICULTY_MENU)) {
                            game.setDifficulty(Difficulty.fromString(option.getOption()));
                            outStream.println("La difficulté a bien été modifiée");
                            this.showMenu(inStream, outStream, game);
                        }

                        // Special case "Play"
                        // TODO
                    }

                    return true;
                }
            }
            return false;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
