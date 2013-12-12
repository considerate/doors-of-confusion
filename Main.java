public class Main {
  public static void main(String[] args) {
    String matchMakerHost = args[0];
    int matchMakerPort = Integer.parseInt(args[1]);
    int port = Integer.parseInt(args[2]);
    int numPlayer = Integer.parseInt(args[3]);
    Game game = new Game(matchMakerHost, matchMakerPort, port);
  }
}