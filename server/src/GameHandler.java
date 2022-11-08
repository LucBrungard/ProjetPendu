import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import game.Game;
import game.GameState;
import menu.MenuFactory;
import menu.Menu;

public class GameHandler extends Thread {
    protected BufferedReader inStream;
    protected PrintStream outStream;
    protected int clientNumber;
    protected Socket socket;

    public GameHandler(Socket socket, int clientNumber) throws IOException {
        this.inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outStream = new PrintStream(socket.getOutputStream());
        this.socket = socket;
        this.clientNumber = clientNumber;
    }
    
    @Override
    public void run() {
        this.outStream.println("Bienvenue dans le jeu du pendu !");
       
        Game game = new Game();

        Menu currentMenu = MenuFactory.getStartMenu();

        do {
            do {
                this.showMessage("Affichage du menu " + currentMenu.getName());
                currentMenu.showMenu(this.inStream, this.outStream, game);
                
                // Handle response
                try {

                    // Get response from client
                    String choice = this.inStream.readLine();
                    if (choice == null) this.connectionInterrupted(game);
                    this.showMessage("message reçu : " + choice);

                    // Let the menu handle it
                    currentMenu = currentMenu.handleChoice(choice, inStream, outStream, game);

                    if (game.getState().equals(GameState.IN_MENU_ERROR)) {
                        this.showMessage("Le choix n'est pas valide");
                        this.outStream.println("Le choix n'est pas valide");
                    }
                } catch (IOException e) {
                    this.connectionInterrupted(game);
                    e.printStackTrace();
                }
            } while (currentMenu != null);
    
            if (game.getState().equals(GameState.STOP)) {
                this.showMessage("Le client a quitté le serveur...");
                this.closeGame();
                return;
            }
                  
            startGame(inStream, outStream, game);

            if (game.getState().equals(GameState.STOP)) {
                this.closeGame();
                return;
            }
    
            this.outStream.println("Fini !");
            this.showMessage("Fin de la partie");

            if (game.getState().equals(GameState.WON)) {
                this.outStream.println("Bravo, vous avez gagné !");
                this.showMessage("Le client a gagné");
            } else if (game.getState().equals(GameState.LOST)) {
                this.outStream.println("Dommage, vous avez perdu !");
                this.showMessage("Le client a perdu");
            }

            this.outStream.println("Le mot à trouver était : " + game.getToFind());
    
            currentMenu = MenuFactory.getEndMenu();
        } while (!game.getState().equals(GameState.STOP));
    }

    private void connectionInterrupted(Game game) {
        game.setState(GameState.STOP);
        this.showMessage("La connexion a été interrompue");
    }

    private void closeGame() {
        this.outStream.println("Aurevoir...");

        try {
            this.inStream.close();
            this.outStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGame(BufferedReader inStream, PrintStream outStream, Game game) {
        this.outStream.println("Commençons !");
        this.outStream.println("Vous pourrez abandonner à tout moment en tapant : " + Game.getForfeitCommand());
        this.showMessage("Début de la partie");

        game.start();
        this.showMessage(game.toString());

        String response;
        while (!game.hasBeenFound() && !game.hasLost()) {
            outStream.println("Etat du mot : " + game.getCurrentStateWord());
            outStream.println("Erreurs : " + game.getErrors() + "/" + game.getMaxErrors());
            outStream.print("Lettres proposées : ");
            game.getAlreadyProposed().forEach(proposed -> outStream.print(proposed + ","));
            outStream.println("\nVotre proposition :");

            try {
                response = inStream.readLine();
                this.showMessage("Le choix est : " + response);
                if (response == null) {
                    this.connectionInterrupted(game);
                    return;
                }

                if (response.equalsIgnoreCase(Game.getForfeitCommand())) {
                    game.setState(GameState.LOST);
                    return;
                }
                
                if (response.length() == 1) {
                    char tmp = response.charAt(0);
                    if (game.validLetter(tmp)) {
                        game.guessLetter(tmp);
                    } else {
                        this.showMessage("La lettre a déjà été proposée");
                        this.outStream.println("La lettre a déjà été proposée");
                    }
                } else {
                    if (game.guessWordToFind(response)) {
                        game.setState(GameState.WON);
                        return;
                    };
                }

                this.showMessage(game.toString());
            } catch (IOException e) {
                game.setState(GameState.STOP);
                this.showMessage("Une erreur est survenue lors de la lecture de la réponse");
                e.printStackTrace();
                return;
            }
        }

        if (game.hasBeenFound()) {
            game.setState(GameState.WON);
        } else {
            game.setState(GameState.LOST);
        }
    }

    private void showMessage(String message) {
        System.out.println("[client " + this.clientNumber + "] : " + message);
    }

}
