package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientManager{
    public static void start(){
        try{
            Client client = new Client();

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Entrez l'adresse IP du serveur : ");
			String ip = keyboard.readLine();
            System.out.println("Entrez le port du serveur : ");
			String port = keyboard.readLine();

            client.startConnection(ip, Integer.parseInt(port));

            String res = client.getBufferedReader().readLine();
            System.out.println(res);

            boolean keep = true;

            do{
                String choix = keyboard.readLine();
                if(choix == ""){
                    keep = false;
                }else{
                    String response = client.sendMessage(choix);
                }
            }while(keep);

            client.stopConnection();
            System.out.println("Disconnected !");

        }catch(IOException e){
			e.printStackTrace();
        }


    }
}