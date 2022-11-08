import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static String ip = "127.0.0.1";
    public static String port = "6666";

    public Client(){}

    public void startConnection(String ip, int port) {
        try{ 
            socket = new Socket(ip, port);
            System.out.println("Connected !");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(UnknownHostException e) {
			e.printStackTrace();
        } catch(IOException e) {
			e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch(IOException e) {
			e.printStackTrace();
        }
    }

    public static int checkInputGame(String character){
        if(Pattern.matches("[a-zA-Z]+", character)){
            return 0;
        }
        if(Pattern.matches(".*[0-9].*", character)){
            return 1;
        }
        return 2;
    }

    public BufferedReader getBufferedReader() {
        return this.in;
    }
}