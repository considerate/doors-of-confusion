package se.considerate.doors.world;

public class Exit {
  private Room room;
  private String type;

  public Exit(Room room, String type) {
    this.room = room;
    this.type = type;
  }

  public Room getRoom() {
    return this.room;
  }

  public String getType() {
    return this.type;
  }

  public boolean canPass(Player player) {
    String color = player.getColor();
    String type = this.type;
    if(type.equals(color)) {
      return true;
    }
    return false;
  }
}