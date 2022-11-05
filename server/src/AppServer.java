import java.io.IOException;
import java.util.logging.Logger;

public class AppServer {
    private static Logger LOGGER = Logger.getLogger("App");
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(6666);
            server.start();
        } catch (IOException e) {
            LOGGER.severe("Server couldn't started :");
            e.printStackTrace();
        }
    }
}
