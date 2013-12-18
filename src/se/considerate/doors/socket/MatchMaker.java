package se.considerate.doors.socket;

import java.net.*;
import java.io.*;

public class MatchMaker {
  public MatchMaker() {
    try {
      ServerSocket serverSocket =  new ServerSocket(8880);
      while(true) {
        //Connect a pair of clients
        Socket firstClient = serverSocket.accept();
        BufferedReader firstIn = new BufferedReader(new InputStreamReader(firstClient.getInputStream()));
        String firstPort = firstIn.readLine();
        Socket secondClient = serverSocket.accept();
        BufferedReader secondIn = new BufferedReader(new InputStreamReader(secondClient.getInputStream()));
        String secondPort = secondIn.readLine();

        //Give eachother the other client's addresses
        PrintWriter firstOut = new PrintWriter(firstClient.getOutputStream(), true);
        PrintWriter secondOut = new PrintWriter(secondClient.getOutputStream(), true);
        String firstAddress = getRemoteAddress(firstClient);
        String secondAddress = getRemoteAddress(secondClient);
        System.out.println(firstAddress+":"+firstPort);
        System.out.println(secondAddress+":"+secondPort);
        //Write back results
        firstOut.println(secondAddress+":"+secondPort+":blue:orange");
        secondOut.println(firstAddress+":"+firstPort+":orange:blue");
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private String getRemoteAddress(Socket socket) {
    InetAddress ip = socket.getInetAddress();
    String host = ip.getHostAddress().toString();  
    return host;
  }

  public static void main(String[] args) {
    new MatchMaker();
  }
}