import java.net.*;
import java.io.*;
import java.util.ArrayList;
 
public class MessageServerThread extends Thread {
    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    private ArrayList<MessageListener> listeners;
 
    public MessageServerThread(Socket socket, ArrayList<MessageListener> listeners) {
        super("MessageServerThread");
        this.socket = socket;
        this.listeners = listeners;
        try
        {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            this.in = new BufferedReader(streamReader);  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try
        {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                for(MessageListener listener: listeners) {
                    listener.messageRecieved(inputLine);
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
