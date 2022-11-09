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
            // Loop on menus whilenot on a leaf
            do {
                this.showMessage("Affichage du menu " + currentMenu.toString());
                currentMenu.showMenu(this.inStream, this.outStream, game);
                
                // Handle response
                try {
                    // Get response from client
                    String choice = this.inStream.readLine();
                    this.debugMessage("message reçu : " + choice);
                    if (choice == null) {
                        this.connectionInterrupted();
                        return;
                    }

                    // Let the menu handle the choice
                    currentMenu = currentMenu.handleChoice(choice, inStream, outStream, game);

                    // If the client has chosen an invalid option
                    if (game.getState().equals(GameState.IN_MENU_ERROR)) {
                        this.debugMessage("Le choix n'est pas valide");
                        this.outStream.println("Le choix n'est pas valide");
                    }
                } catch (IOException e) {
                    this.connectionInterrupted();
                    e.printStackTrace();
                    return;
                }
            } while (currentMenu != null);
    
            // If the client has chosen the quit option
            if (game.getState().equals(GameState.STOP)) {
                this.debugMessage("Le client a quitté le serveur...");
                this.outStream.println("Aurevoir...");
                this.closeGame();
                return;
            }
                  
            startGame(inStream, outStream, game);

            if (game.getState().equals(GameState.STOP)) {
                this.connectionInterrupted();
                return;
            }
    
            this.outStream.println("Fini !");
            this.debugMessage("Fin de la partie");

            if (game.getState().equals(GameState.WON)) {
                this.outStream.println("Bravo, vous avez gagné !");
                this.debugMessage("Le client a gagné");
            } else if (game.getState().equals(GameState.LOST)) {
                this.outStream.println("Dommage, vous avez perdu !");
                this.debugMessage("Le client a perdu");
            }

            this.outStream.println("Le mot à trouver était : " + game.getToFind());
    
            currentMenu = MenuFactory.getEndMenu();
        } while (!game.getState().equals(GameState.STOP));
    }
 
    private void startGame(BufferedReader inStream, PrintStream outStream, Game game) {
        this.outStream.println("Commençons !");
        this.outStream.println("Vous pourrez abandonner à tout moment en tapant : " + Game.getForfeitCommand());
        this.debugMessage("Début de la partie");

        game.start();
        this.debugMessage(game.toString());

        String response;
        while (!game.hasBeenFound() && !game.hasLost()) {
            outStream.println("Etat du mot : " + game.getCurrentStateWord());
            outStream.println("Erreurs : " + game.getErrors() + "/" + game.getMaxErrors());
            outStream.print("Lettres proposées : ");
            game.getAlreadyProposed().forEach(proposed -> outStream.print(proposed + ","));
            outStream.println("\nVotre proposition :");

            try {
                response = inStream.readLine();
                this.debugMessage("Le choix est : " + response);

                if (response == null) {
                    game.setState(GameState.STOP);
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
                        this.debugMessage("La lettre a déjà été proposée");
                        this.outStream.println("La lettre a déjà été proposée");
                    }
                } else {
                    if (game.guessWordToFind(response)) {
                        game.setState(GameState.WON);
                        return;
                    };
                }

                this.debugMessage(game.toString());
            } catch (IOException e) {
                game.setState(GameState.STOP);
                this.debugMessage("Une erreur est survenue lors de la lecture de la réponse");
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

    private void connectionInterrupted() {
        this.debugMessage("La connexion a été interrompue");
        this.closeGame();
    }

    private void closeGame() {
        try {
            this.inStream.close();
            this.outStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private void debugMessage(String message) {
        System.out.println("[client " + this.clientNumber + "] : " + message);
    }
}
