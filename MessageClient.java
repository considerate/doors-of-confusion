import java.net.*;
import java.io.*;

import java.util.ArrayList;

interface ClientListener {
  public void clientConnected();
  public void colorSelected(String color,String otherColor);
}

public class MessageClient {
  private Socket socket;
  private PrintWriter out;
  private ArrayList<ClientListener> listeners;

  public MessageClient() {
    listeners = new ArrayList<ClientListener>();
  }

  public void connect(String host, int port, int serverPort) {
    try {
      Socket matchMakerSocket = new Socket(host,port);

      BufferedReader matchIn = new BufferedReader(new InputStreamReader(matchMakerSocket.getInputStream())); 
      PrintWriter matchOut = new PrintWriter(matchMakerSocket.getOutputStream(), true);
      matchOut.println(serverPort);
      //Block until we get response
      String matchLine = matchIn.readLine();

      //Create socket with info from server
      String[] parts = matchLine.split(":");
      String clientHost = parts[0];
      int clientPort = Integer.parseInt(parts[1]);
      String myColor = parts[2];
      String otherColor = parts[3];

      for(ClientListener listener: listeners) {
        listener.colorSelected(myColor,otherColor);
      }
      //System.out.println(clientHost);
      //System.out.println(clientPort);
      Socket socket = new Socket(clientHost,clientPort);
      OutputStream outputStream = socket.getOutputStream();
      this.out = new PrintWriter(outputStream, true);
      this.socket = socket;

      for(ClientListener listener: listeners) {
        listener.clientConnected();
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public void addEventListener(ClientListener listener) {
    listeners.add(listener);
  }

  public void sendMessage(String message) {
    this.out.println(message);
  }
}