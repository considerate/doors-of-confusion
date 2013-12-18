package se.considerate.doors.world;

import java.util.HashMap;

public class GameMap {
  private HashMap<String,Room> rooms;
  private Room firstRoom;


  public GameMap() { 
    this.rooms = new HashMap<String,Room>();
  }

  public void addRoom(String id, Room room) {
    rooms.put(id, room);
  }

  public HashMap<String,Room> getRooms() {
    return this.rooms;
  }

  public void setFirstRoom(Room room) {
    this.firstRoom = room;
  }

  public Room getFirstRoom() {
    return this.firstRoom;
  }
}
