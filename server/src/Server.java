import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

public class Server extends ServerSocket {
    private int nbClient = 0;
    private boolean state = false; 

    public Server(int port) throws IOException {
        super(port);

        // Print possible ip addresses
        System.out.println("=============================");
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
            }
        }
        System.out.println("=============================\n");

        System.out.println("Le serveur est prêt au port : " + this.getLocalSocketAddress());
    }

    public void start() {
        this.state = true;
        Socket socket;

        while(state) {
            System.out.println("En attente d'un nouveau client...");

            try {
                socket = this.accept();

                try {
                    GameHandler gameHandler = new GameHandler(socket, this.nbClient);
                    // RequestHandler requestHandler = new RequestHandler(socket, this.nbClient);
                    
                    System.out.println("Nouvelle connexion réussi avec le client n°" + this.nbClient++);

                    gameHandler.start();
                    // requestHandler.start();
                } catch (IOException re) {
                    System.err.println("Une erreur est survenue lors de la création d'un nouveau client : ");
                    re.printStackTrace();
                } 
            } catch (SocketTimeoutException e) {
                // Nothing to do
            } catch (IOException e) {
                System.err.println("Une erreur est survenue lors de l'attente du client");
                e.printStackTrace();   
            }
        }
    }

    public void stop() {
        this.state = false;
    }
}
