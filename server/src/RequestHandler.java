import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Logger;

import exceptions.RequestHandlerException;

public class RequestHandler extends Thread {
    private BufferedReader inStream;
    private PrintStream outStream;
    private int clientNumber;

    private Logger logger;

    public RequestHandler(Socket socket, int clientNumber) throws IOException {
        this.inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outStream = new PrintStream(socket.getOutputStream());
        this.clientNumber = clientNumber;
        this.logger = Logger.getLogger("RequestHandler n°" + this.clientNumber);
    }

    @Override
    public void run() {
        String request;
        while (!this.isInterrupted()) {
            try {
                request = this.inStream.readLine();
                if (request == null)
                    throw new RequestHandlerException("Client n°" + this.clientNumber + " has been disconnected");

                logger.info("Client has sent : " + request);

                this.outStream.println("echo");
            } catch (RequestHandlerException e) {
                logger.info(e.getMessage());
                e.printStackTrace();
                this.interrupt();
            } catch (IOException e) {
                logger.severe("An I/O error occured when reading client message : ");
                e.printStackTrace();
            }
        }

        try {
            this.close();
        } catch (IOException e) {
            logger.severe("An exception as occured closing streams : ");
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        this.inStream.close();
        this.outStream.close();
    }
}
