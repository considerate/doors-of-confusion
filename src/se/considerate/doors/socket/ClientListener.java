package se.considerate.doors.socket;

public interface ClientListener {
  public void clientConnected();
  public void colorSelected(String color,String otherColor);
}