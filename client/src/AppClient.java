import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppClient {
    public static void main(String[] args) throws Exception {
        try {
            Client client = new Client();

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Entrez l'adresse IP du serveur : ");
            String ip = "127.0.0.1";
			// String ip = keyboard.readLine();
            System.out.println("Entrez le port du serveur : ");
            String port = "6666";
			// String port = keyboard.readLine();

            client.startConnection(ip, Integer.parseInt(port));

            String response;
            while (true) {
                menu:
                while (true) {
                    // Handle 1 menu
                    do {
                        response = client.getBufferedReader().readLine();
                        System.out.println(response);
                        if (response.endsWith("...")) System.exit(0);
                        else if (response.endsWith("Commençons !")) break menu;
                    } while (!response.endsWith("?"));

                    // Send choice for 1 menu
                    String choice = keyboard.readLine();
                    client.sendMessage(choice);
                    System.out.println();
                }

                gamephase:
                while (true) {
                    // Handle 1 game phase
                    do {
                        response = client.getBufferedReader().readLine();
                        System.out.println(response);
                        if (response.endsWith("Fini !")) break gamephase;
                    } while (!response.endsWith(":"));

                    // Send choice for 1 gamephase
                    String choice = keyboard.readLine();
                    client.sendMessage(choice);
                    System.out.println();
                }
            }

            // menu:
            // while (true) {
            //     do {
            //         response = client.getBufferedReader().readLine();
            //         System.out.println(response);
            //         if (response.endsWith("...")) System.exit(0);

            //         if (response.endsWith("Votre proposition :")) break menu;
            //     } while (!response.endsWith("?"));
    
            //     String choice = keyboard.readLine();
            //     client.sendMessage(choice);
            //     System.out.println();
            // }
            
            // while (true) {
            //     String choice = keyboard.readLine();
            //     client.sendMessage(choice);
            //     System.out.println();

            //     do {
            //         response = client.getBufferedReader().readLine();
            //         System.out.println(response);
            //     } while (!response.toString().endsWith("Votre proposition :"));
            // }

            // do {
            //     System.out.println("Veuillez écrire un message");
            //     String choix = keyboard.readLine();
            //     if(choix == ""){
            //         keep = false;
            //     }else{
            //         String response = client.sendMessage(choix);
            //         System.out.println("response : " + response);
            //         if (response == null) {
            //             keep = false;
            //         }
            //     }
            // } while(keep);

            // client.stopConnection();
            // System.out.println("Disconnected !");

        } catch(IOException e) {
			e.printStackTrace();
        }
    }
}
