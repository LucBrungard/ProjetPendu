import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppClient {
    public static void main(String[] args) throws Exception {
        try {
            Client client = new Client();

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Entrez l'adresse IP du serveur : ");
			String ip = keyboard.readLine();
            System.out.println("Entrez le port du serveur : ");
			String port = keyboard.readLine();

            client.startConnection(ip, Integer.parseInt(port));

            boolean keep = true;

            do {
                System.out.println("Veuillez écrire un message");
                String choix = keyboard.readLine();
                if(choix == ""){
                    keep = false;
                }else{
                    String response = client.sendMessage(choix);
                    System.out.println("response : " + response);
                    if (response == null) {
                        keep = false;
                    }
                }
            } while(keep);

            client.stopConnection();
            System.out.println("Disconnected !");

        } catch(IOException e) {
			e.printStackTrace();
        }
    }
}
