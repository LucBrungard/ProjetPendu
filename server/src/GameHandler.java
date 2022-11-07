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
                currentMenu = currentMenu.showMenu(this.inStream, this.outStream, game);
            } while (currentMenu != null);
    
            if (game.getState().equals(GameState.STOP)) {
                this.closeGame();
                return;
            }
    
            this.outStream.println("Commençons !");
                
            startGame(inStream, outStream, game);
    
            this.outStream.println("Fini !");
    
            if (game.getState().equals(GameState.WON)) {
                this.outStream.println("Bravo, vous avez gagné !");
            } else if (game.getState().equals(GameState.LOST)) {
                this.outStream.println("Dommage, vous avez perdu !");
            }
            this.outStream.println("Le mot à trouver était : " + game.getToFind());
    
            currentMenu = MenuFactory.getEndMenu();
        } while (!game.getState().equals(GameState.STOP));

        // GameState gameState = startMenu.showMenu(this.inStream, this.outStream, game);
        // gameState = MenuFactory.getEndMenu().showMenu(inStream, outStream, game);
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
        game.start();

        String response;
        while (!game.hasBeenFound() && !game.hasLost()) {
            outStream.println("Etat du mot : " + game.getCurrentStateWord());
            outStream.println("Erreurs : " + game.getErrors() + "/" + game.getMaxErrors());
            outStream.print("Lettres proposées : ");
            game.getAlreadyProposed().forEach(proposed -> outStream.print(proposed + ","));
            outStream.println("\nVotre proposition :");

            try {
                response = inStream.readLine();
                if (response == null) {
                    game.setState(GameState.STOP);
                    return;
                }   
                
                if (response.length() == 1) {
                    game.guessLetter(response.charAt(0));
                } else {
                    if (game.guessWordToFind(response)) {
                        game.setState(GameState.WON);
                        return;
                    };
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (game.hasBeenFound()) {
            game.setState(GameState.WON);
            // outStream.println("GG !");
        } else {
            game.setState(GameState.LOST);
            // outStream.println("Perdu ! Le mot à trouver était : " + game.getToFind());
        }
    }

}
