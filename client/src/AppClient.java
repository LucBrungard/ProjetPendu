import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppClient {
   public static void main(String[] args) throws Exception {
      try {
         Client client = new Client();

         BufferedReader keyboard = new BufferedReader(
               new InputStreamReader(System.in));

         System.out.println("Entrez l'adresse IP du serveur : ");
         // String ip = "127.0.0.1";
         String ip = keyboard.readLine();
         System.out.println("Entrez le port du serveur : ");
         String port = "6666";
         // String port = keyboard.readLine();
         System.out.println("En cours de connexion");

         client.startConnection(ip, Integer.parseInt(port));

         String response;
         while (true) {
            menu: while (true) {
               // Handle 1 menu
               do {
                  response = client.getBufferedReader().readLine();
                  System.out.println(response);
                  if (response.endsWith("..."))
                     System.exit(0);
                  else if (response.endsWith("Commençons !"))
                     break menu;
               } while (!response.endsWith("?"));

               // Send choice for 1 menu
               String choice = keyboard.readLine();
               System.out.println(Client.checkInputGame(choice));
               client.sendMessage(choice);
               System.out.println();
            }

            gamephase: while (true) {
               // Handle 1 game phase
               do {
                  response = client.getBufferedReader().readLine();
                  System.out.println(response);
                  if (response.endsWith("Fini !"))
                     break gamephase;
               } while (!response.endsWith(":"));

               // Send choice for 1 gamephase
               String choice = "";

               while (Client.checkInputGame(choice) != 0) {
                  choice = keyboard.readLine();

                  switch (Client.checkInputGame(choice)) {
                     case 0:
                        client.sendMessage(choice);
                        System.out.println();
                        break;
                     case 1:
                        System.out.println(
                              "Veuillez reformuler sans aucun chiffre.");
                        break;
                     case 2:
                        System.out.println(
                              "Veuillez reformuler sans aucun caractère spécial.");
                        break;

                  }
               }

            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
