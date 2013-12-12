import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Room 
{
    private String description;
    private HashMap<String, Exit> exits;        // stores exits of this room.
    private ArrayList<Item> items;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Exit>();
        items = new ArrayList<Item>();
    }

    /**
     * Add a new Item to the items in this room.
     * @param item The item to add.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Get a list of the items in this room.
     * @return the list of items.
     */
    public ArrayList<Item> getItems() {
        return this.items;
    }

    /**
     *  Get a map of all exits with the direction as key.
     */
    public HashMap<String, Exit> getExits() {
        return this.exits;
    }
    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Exit neighbor) 
    {
        String key = direction.toLowerCase();
        exits.put(key, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";


        Iterator entries = exits.entrySet().iterator();
        while (entries.hasNext()) {
          Entry thisEntry = (Entry) entries.next();
          String direction = (String) thisEntry.getKey();
          Exit exit = (Exit) thisEntry.getValue();
          String color = exit.getType();
          returnString += " " + direction + " ("+color+")";
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Exit getExit(String direction) 
    {
        String key = direction.toLowerCase();
        return exits.get(key);
    }
}

