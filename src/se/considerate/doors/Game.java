package se.considerate.doors;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Set;

import java.io.PrintStream;
import java.io.File;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import se.considerate.doors.command.Command;
import se.considerate.doors.command.CommandListener;
import se.considerate.doors.command.CommandRunner;
import se.considerate.doors.command.Parser;

import se.considerate.doors.socket.MessageClient;
import se.considerate.doors.socket.ClientListener;
import se.considerate.doors.socket.MessageListener;
import se.considerate.doors.socket.MessageServer;

import se.considerate.doors.world.Player;
import se.considerate.doors.world.PlayerListener;
import se.considerate.doors.world.MapParser;
import se.considerate.doors.world.GameMap;
import se.considerate.doors.world.Room;
import se.considerate.doors.world.Exit;
import se.considerate.doors.world.Item;


public class Game implements MessageListener, CommandListener, ClientListener
{
    private Parser commandParser;
    private MapParser mapParser;
    private GameMap map;
    private MessageServer server;
    private MessageClient client;
    private String remoteHost;
    private Player me;
    private Player playerTwo;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game(String matchMakerHost, int matchMakerPort, int port) 
    {
        GameMap map = createMap();
        
        commandParser = new Parser();
        commandParser.addEventListener(this);

        server = new MessageServer(port);
        server.addEventListener(this);
        client = new MessageClient();
        client.addEventListener(this);

        me = new Player();
        me.setCurrentRoom(map.getFirstRoom());
        me.addEventListener(new PlayerListener() {
          public void characterCreated(Room firstRoom) {
            System.out.println();
            System.out.println("You have stepped into the world of doors of confusion!");
            System.out.println("You spot a weird character in the distance. He calls your name.");
            System.out.println("Type 'help' if you need help.");
            System.out.println();
            System.out.println(firstRoom.getLongDescription());
          }
          public void helpRequested(HashMap<String,CommandRunner> commands) {
            System.out.println("You can only pass through doors with the same color as your hat.");
            System.out.println("Your hat is "+me.getColor()+".");
            System.out.println();
            System.out.println("Your command words are:");

            Set<String> commandWords = commands.keySet();
            for(String word: commandWords) {
                System.out.print(word+" ");
            }
            System.out.print("\n");
          }
          public void roomEntered(Room room) {
            System.out.println(room.getLongDescription());
            System.out.println("The room has started burning. You have less than " + Player.timePerRoom +" seconds to leave.");

            boolean trapped = true;
            for(Exit exit: room.getExits().values()) {
                if(exit.canPass(me)) {
                    trapped = false;
                    break;
                }
            }
            if(trapped) {
                System.out.println("It's a trap!");
                playAudio("data/trap.wav");
            }
          }

          public void inspectRoom(Room room) {
            System.out.println("Looking around the room...");
            ArrayList<Item> items = room.getItems();
            if(items.size() > 0) {
                System.out.println("There are some items here.\n");
                for(Item item: items) {
                    System.out.println(item);
                }
            }
          }

          public void inspectFailed(String command) {
            System.out.println("Failed to inspect "+command);
          }

          public void exitPassed(String direction, Exit exit) {
            if("west".equals(direction)) {
                playAudio("data/gowest.aif");
            }
          }

          public void cantPass() {
            System.out.println("You shall not pass!!!!");
            playAudio("data/lotr.aif");
          }

          public void quitGame() {
            System.out.println("You're a quitter. You gave up and committed suicide.");
            System.exit(0);
          }
          public void timeUp() {
            System.out.println();
            System.out.println("The university burst into flames. You died a gruesome death.");
          }

          public void commandUnknown(String command) {
            System.out.println("I don't know what you mean...");
          }

          public void roomNotFound() {
            System.out.println("There's no exit in that direction");
          }
        });
        playerTwo = new Player();
        playerTwo.setCurrentRoom(map.getFirstRoom());

        client.connect(matchMakerHost, matchMakerPort, port);
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private GameMap createMap()
    {
        mapParser = new MapParser("data/map.json");
        GameMap map = mapParser.parseMap();
        return map;
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        me.welcome();
        Thread commandParserThread = new Thread(commandParser);
        commandParserThread.start();
        Thread messageServerMainThread = new Thread(server);
        messageServerMainThread.start();
    }

    public void playAudio(String filename)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
    }

    @Override
    public void messageRecieved(String message) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject object = (JSONObject) jsonParser.parse(message);
            String commandString = (String) object.get("command");
            Command command = commandParser.parseCommand(commandString);
            playerTwo.processCommand(command);
        } catch(Exception e) {

        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public void commandRecieved(Command command) {
        me.processCommand(command);

        if(!command.isUnknown()) {
            JSONObject object = new JSONObject();
            object.put("command", command.toString());
            object.put("type","command");
            object.put("time", System.nanoTime());
            client.sendMessage(object.toString());
        }
    }

    @Override
    public void clientConnected() {
    }

    @Override
    public void colorSelected(String color, String otherColor) {
        System.out.println("Hello, Mr. "+color);
        me.setColor(color);
        playerTwo.setColor(otherColor);
        play();
    }
}
