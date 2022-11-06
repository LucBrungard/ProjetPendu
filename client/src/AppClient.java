import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppClient {
    public static void main(String[] args) throws Exception {
        try {
            Client client = new Client();

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Entrez l'adresse IP du serveur : ");
            String ip = "192.168.1.31";
			// String ip = keyboard.readLine();
            System.out.println("Entrez le port du serveur : ");
            String port = "6666";
			// String port = keyboard.readLine();

            client.startConnection(ip, Integer.parseInt(port));

            boolean keep = true;

            while (true) {
                String response;
                do {
                    response = client.getBufferedReader().readLine();
                    System.out.println(response);
                    if (response.endsWith("...")) System.exit(0);
                } while (!response.endsWith("?"));
    
                String choice = keyboard.readLine();
                client.sendMessage(choice);
                System.out.println();
            }

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
