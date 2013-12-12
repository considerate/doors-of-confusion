import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.lang.Exception;

import java.util.HashMap;
import java.util.ArrayList;


/**
 * A class for parsing a JSON file and 
 * loading it as a Map with containing rooms.
 */


public class MapParser {
  JSONObject mapJSON;

  public MapParser(String filename) {
    try {
      String json = readFile(filename, StandardCharsets.UTF_8);
      JSONParser jsonParser = new JSONParser();
      mapJSON = (JSONObject) jsonParser.parse(json);
    } catch(Exception e) {

    }
  }

  public GameMap parseMap() {
    GameMap map = new GameMap();

    JSONArray roomsArray = (JSONArray) mapJSON.get("rooms");
    if(roomsArray != null) {

      //Create rooms
      for(Object obj: roomsArray) {
        JSONObject roomJSON = (JSONObject) obj;
        Room room = parseRoom(roomJSON);
        String id = (String) roomJSON.get("id");
        map.addRoom(id,room);
      }

      //Set exits
      HashMap<String, Room> rooms = map.getRooms();
      for(Object obj: roomsArray) {
        JSONObject roomJSON = (JSONObject) obj;
        String id = (String) roomJSON.get("id");
        Room room = rooms.get(id);
        setExits(room, rooms, roomJSON);
      }

      //Set starting room
      String startingRoom = (String) mapJSON.get("startingRoom");
      Room firstRoom = rooms.get(startingRoom);
      map.setFirstRoom(firstRoom);
    }

    return map;
  } 

  private void setExits(Room room, HashMap<String, Room> rooms, JSONObject roomJSON) {
    JSONArray exits = (JSONArray) roomJSON.get("exits");
    if(exits != null) {
      for(Object obj: exits) {
        JSONObject exitJSON = (JSONObject) obj;
        String label = (String) exitJSON.get("label");
        String roomID = (String) exitJSON.get("roomID");
        String color = (String) exitJSON.get("type");

        Room exitRoom = rooms.get(roomID);
        Exit exit = new Exit(exitRoom,color);
        room.setExit(label,exit);
      }
    }
  }

  private Room parseRoom(JSONObject roomJSON) {
    String description = (String) roomJSON.get("description");
    Room room = new Room(description);

    JSONArray itemArray = (JSONArray) roomJSON.get("items");
    if(itemArray != null) {
      for(Object obj: itemArray) {
        JSONObject itemJSON = (JSONObject) obj;
        Item item = parseItem(itemJSON);
        room.addItem(item);
      }
    }

    return room;
  }

  private Item parseItem(JSONObject itemJSON) {
    String name = (String) itemJSON.get("name");
    String type = (String) itemJSON.get("type");
    Item item = null;
    if(type.equals("weapon")) {
      Weapon weapon = new Weapon(name);
      item = weapon;
    } else if(type.equals("potion")) {
      Potion potion = new Potion(name);
      item = potion;
    }
    return item;
  }

  private Character parseCharacter(JSONObject characterJSON) {
    return null;
  }

  static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return encoding.decode(ByteBuffer.wrap(encoded)).toString();
  }
}