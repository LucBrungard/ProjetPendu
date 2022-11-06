import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class Server extends ServerSocket {
    private final static Logger LOGGER = Logger.getLogger("server");
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
        System.out.println("=============================");

        LOGGER.info("Server is ready at : " + this.getLocalSocketAddress());
    }

    public void start() {
        this.state = true;
        Socket socket;

        while(state) {
            System.out.println("waiting for a new client...");

            try {
                socket = this.accept();

                try {
                    GameHandler gameHandler = new GameHandler(socket, this.nbClient);
                    // RequestHandler requestHandler = new RequestHandler(socket, this.nbClient);
                    
                    LOGGER.info("New connection made with client n°" + this.nbClient++);

                    gameHandler.start();
                    // requestHandler.start();
                } catch (IOException re) {
                    LOGGER.severe("An error has occured when creating a request handler : ");
                    re.printStackTrace();
                } 
            } catch (SocketTimeoutException e) {
                // Nothing to do
            } catch (IOException e) {
                LOGGER.severe("An error as occured when waiting for a client");
                e.printStackTrace();   
            }
        }
    }

    public void stop() {
        this.state = false;
    }
    
}
