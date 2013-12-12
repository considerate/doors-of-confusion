import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MessageServer implements Runnable {
  private ServerSocket serverSocket;
  private ArrayList<MessageListener> listeners;

  public MessageServer(int port) {
    listeners = new ArrayList<MessageListener>();
    try {
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println("Failed to listen on port: " + port);
      System.exit(-1);
    }
  }

  public void addEventListener(MessageListener listener) {
    listeners.add(listener);
  }

  public void run () {
    try{
      while (true) {
        Socket socket = serverSocket.accept();
        MessageServerThread thread = new MessageServerThread(socket, listeners);
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
