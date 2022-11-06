import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
// import java.util.logging.Logger;

import game.Game;
import game.settings.Difficulty;
import menu.MenuFactory;
import menu.Menu;

public class GameHandler extends Thread {
    protected String word;
    protected BufferedReader inStream;
    protected PrintStream outStream;
    protected int clientNumber;
    protected Socket socket;

    // private Logger logger;

    public GameHandler(Socket socket, int clientNumber) throws IOException {
        this.inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outStream = new PrintStream(socket.getOutputStream());
        this.socket = socket;
        this.clientNumber = clientNumber;

        // this.logger = Logger.getLogger(this.getName());
    }
    
    @Override
    public void run() {
        this.outStream.println("Bienvenue dans le jeu du pendu !");
       
        Game game = new Game(Difficulty.NORMAL);
        Menu startMenu = MenuFactory.getStartMenu();

        startMenu.showMenu(this.inStream, this.outStream, game);

        try {
            this.inStream.close();
            this.outStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
