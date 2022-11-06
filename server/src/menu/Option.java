package menu;

import java.io.BufferedReader;
import java.io.PrintStream;

import game.Game;

public class Option {
    private int index;
    private String question;
    private Menu previous;
    private Menu next;

    public Option(int index, String question) {
        this.index = index;
        this.question = question;
    }

    public boolean isValid(int choice, BufferedReader inStream, PrintStream outStream, Game game) {
        return this.index == choice;
    }

    public void addPrevious(Menu menuOption) {
        if (menuOption != null) {
            this.previous = menuOption;
        }
    }

    public void addNext(Menu menuOption) {
        if (menuOption != null) {
            this.next = menuOption;
        }
    }

    @Override
    public String toString() {
        return index + ". " + question;
    }

    public String getOption() {
        return this.question;
    }

    public Menu getPrevious() {
        return this.previous;
    }

    public Menu getNext() {
        return this.next;
    }
}
