package menu;

public class Option {
    private int index;
    private String question;
    private Menu next;

    public Option(int index, String question) {
        this.index = index;
        this.question = question;
    }

    public boolean isValid(int choice) {
        return this.index == choice;
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

    public Menu getNext() {
        return this.next;
    }
}
