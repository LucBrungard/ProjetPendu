package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import game.settings.Difficulty;
import menu.MenuFactory;
import scores.ScoresHandler;
import menu.Menu;

public class GameHandler extends Thread {
   protected BufferedReader inStream;
   protected PrintStream outStream;
   protected int clientNumber;
   protected Socket socket;

   protected ClientState clientState = ClientState.IN_MENU_PROGRESS;
   protected GameSetting settings = new GameSetting();
   Game game;

   public GameHandler(Socket socket, int clientNumber) throws IOException {
      this.inStream = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
      this.outStream = new PrintStream(socket.getOutputStream());
      this.socket = socket;
      this.clientNumber = clientNumber;
   }

   @Override
   public void run() {
      this.outStream.println("Bienvenue dans le jeu du pendu !");

      Menu currentMenu = MenuFactory.getStartMenu();
      do {
         this.loopOnMenu(currentMenu);

         if (this.clientState.equals(ClientState.DISCONNECTED)) {
            this.connectionInterrupted();
            return;
         } else if (this.clientState.equals(ClientState.QUIT)) {
            this.debugMessage("Le client a quitté le serveur...");
            this.outStream.println("Aurevoir...");
            this.closeGame();
            return;
         }

         startGame();

         if (this.game.getState().equals(GameState.STOPPED)) {
            this.connectionInterrupted();
            return;
         }

         this.outStream.println("Fini !");
         this.debugMessage("Fin de la partie");

         if (this.game.getState().equals(GameState.WON)) {
            this.outStream.println("Bravo, vous avez gagné !");
            this.debugMessage("Le client a gagné");
         } else if (this.game.getState().equals(GameState.LOST)) {
            this.outStream.println("Dommage, vous avez perdu !");
            this.debugMessage("Le client a perdu");
         }

         this.outStream.println("Le mot à trouver était : " + game.getToFind());

         if (this.game.getMode().equals("rush")) {
            displayRush();
         }

         this.clientState = ClientState.IN_MENU_PROGRESS;
         currentMenu = MenuFactory.getEndMenu();
      } while (!this.clientState.equals(ClientState.QUIT));
   }

   private void loopOnMenu(Menu currentMenu) {
      // Loop on menus whilenot on a leaf
      do {
         this.debugMessage("Affichage du menu " + currentMenu.toString());
         currentMenu.showMenu(this.inStream, this.outStream, this.settings);

         // Handle response
         try {
            // Get response from client
            String choice = this.inStream.readLine();
            this.debugMessage("message reçu : " + choice);
            if (choice == null) {
               this.clientState = ClientState.DISCONNECTED;
               return;
            }

            // Let the menu handle the choice
            currentMenu = currentMenu.handleChoice(choice, this);

            // If the client has chosen an invalid option
            if (clientState.equals(ClientState.IN_MENU_ERROR)) {
               this.debugMessage("Le choix n'est pas valide");
               this.outStream.println("Le choix n'est pas valide");
            }
         } catch (IOException e) {
            this.clientState = ClientState.DISCONNECTED;
            e.printStackTrace();
            return;
         }
      } while (currentMenu != null);
   }

   private void displayRush() {
      this.outStream.println(this.game.msgSpecialRule());
      this.outStream.println("Souhaitez-vous enregistrer votre score ? y/n ?");
      this.debugMessage("Demande d'enregistrer le score");

      String response = "";
      do {
         try {
            response = inStream.readLine();

            this.debugMessage("Le choix est : " + response);
            if (response == null) {
               game.setState(GameState.STOPPED);
               return;
            } else if (response.equals("y")) {
               this.outStream.println("Enregistrer sous le nom de ?");
               String resp = inStream.readLine();

               ScoresHandler.getInstance().addScore(this.game.getDifficulty(),
                     resp, this.game.getScore());
               showLeaderBoard(this.game.getDifficulty());
            } else if (response.equals("n")) {
               this.debugMessage("Aucun enregistrement");
               return;
            } else {
               this.debugMessage("La réponse est invalide");
               this.outStream.println("La réponse est invalide");
            }
         } catch (IOException e) {
            e.printStackTrace();
         }

      } while (!response.equals("y") && !response.equals("n"));

   }

   private void startGame() {
      this.outStream.println("Commençons !");
      this.outStream
            .println("Vous pourrez abandonner à tout moment en tapant : "
                  + Game.getForfeitCommand());
      this.outStream
            .println("Vous pourrez demander de l'aide à tout moment (invocable une seule fois par partie) en tapant : "
                  + Game.getHelpCommand());

      this.debugMessage("Début de la partie");

      this.game.start();
      this.debugMessage(game.toString());

      String response;
      while (this.game.getState().equals(GameState.RUNNING)) {
         outStream.println("Etat du mot : " + game.getCurrentStateWord());
         outStream.println(
               "Erreurs : " + game.getErrors() + "/" + game.getMaxErrors());
         outStream.print("Lettres proposées : ");
         game.getAlreadyProposed()
               .forEach(proposed -> outStream.print(proposed + ","));

         outStream.println("\n" + this.game.msgSpecialRule());
         outStream.println("\nVotre proposition :");

         try {
            response = inStream.readLine();
            this.debugMessage("Le choix est : " + response);

            if (response == null) {
               game.setState(GameState.STOPPED);
               return;
            } else if (response.equalsIgnoreCase(Game.getForfeitCommand())) {
               game.setState(GameState.LOST);
               return;
            } else if (response.equalsIgnoreCase(Game.getHelpCommand())) {
               if (this.game.isHelpInvoked()) {
                  outStream.println("Vous avez déjà invoqué la commande d'aide durant cette partie");
               } else {
                  outStream.println("Voici un indice : " + this.game.invokeHelp());
               }
            } else if (response.length() == 1) {
               char tmp = response.charAt(0);
               if (game.validLetter(tmp)) {
                  game.guessLetter(tmp);
               } else {
                  this.debugMessage("La lettre a déjà été proposée");
                  this.outStream.println("La lettre a déjà été proposée");
               }
            } else {
               game.guessWordToFind(response);
               System.out.println(game.getState());
            }

            this.debugMessage(game.toString());
         } catch (IOException e) {
            this.game.setState(GameState.STOPPED);
            this.debugMessage(
                  "Une erreur est survenue lors de la lecture de la réponse");
            e.printStackTrace();
            return;
         }
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

   public void showLeaderBoard(Difficulty difficulty) {
      try {
         this.outStream.println(
               ScoresHandler.getInstance().getLeaderBoardString(difficulty));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void debugMessage(String message) {
      System.out.println("[client " + this.clientNumber + "] : " + message);
   }

   public void setGame(Game game) {
      this.game = game;
   }

   public GameSetting getSettings() {
      return this.settings;
   }

   public PrintStream getOutStream() {
      return this.outStream;
   }

   public void setClientState(ClientState state) {
      this.clientState = state;
   }
}
