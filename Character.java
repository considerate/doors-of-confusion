import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.io.PrintStream;


interface CharacterListener {
  public void characterCreated(Room firstRoom);
  public void helpRequested(HashMap<String,CommandRunner> commands);
  public void roomEntered(Room room);
  public void quitGame();
  public void timeUp();
  public void cantPass();
  public void exitPassed(String direction, Exit exit);

  public void commandUnknown(String command);
  public void roomNotFound();
}

public class Character {
  private float health;
  private Room currentRoom;
  private long startTime;
  private Timer timer;
  public static long timePerRoom;
  private HashMap<String,CommandRunner> commands;
  private ArrayList<CharacterListener> listeners;
  private String color;

  public Character() {
    listeners = new ArrayList<CharacterListener>();
    this.health = 100;

    setupCommands();

    timer = new Timer();
    timePerRoom = 30; //seconds
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getColor() {
    return this.color;
  }

  public void addEventListener(CharacterListener listener) {
    this.listeners.add(listener);
  }

  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  public void setHealth(float health) {
    this.health = health;
  }

  public void setupCommands() {
    commands = new HashMap<String, CommandRunner>();

    commands.put("help", new CommandRunner () {
      public void run (Command command) {
        printHelp();
      }
    });
    commands.put("go", new CommandRunner () {
      public void run (Command command) {
        goRoom(command);
      }
    });
    commands.put("inspect", new CommandRunner () {
      public void run (Command command) {
        //inspect(command);
      }
    });
    commands.put("use", new CommandRunner () {
      public void run (Command command) {
        //TODO: implement use command
      }
    });
    commands.put("pickup", new CommandRunner () {
      public void run (Command command) {
        //pickupObject(command);
      }
    });
    commands.put("quit", new CommandRunner () {
      public void run (Command command) {
        quit(command);
      }
    });
  }

  /**
   * Given a command, process (that is: execute) the command.
   * @param command The command to be processed.
   * @return true If the command ends the game, false otherwise.
   */
  public void processCommand(Command command) {
    String commandWord = command.getCommandWord();
    CommandRunner runner = commands.get(commandWord);
    if (command.isUnknown() || runner == null) {
      for(CharacterListener listener: listeners) {
        listener.commandUnknown(commandWord);
      }
      return;
    }
    runner.run(command);
  }


  /**
   * Print out the opening message for the player.
   */
  public void welcome()
  {
    Room firstRoom = currentRoom;
    for(CharacterListener listener: listeners) {
      listener.characterCreated(firstRoom);
    }
  }

  /**
   * Print out some help information.
   * Here we print some stupid, cryptic message and a list of the
   * command words.
   */
  public void printHelp() {
    for(CharacterListener listener: listeners) {
      listener.helpRequested(commands);
    }
  }

  /**
   * Try to in to one direction. If there is an exit, enter the new
   * room, otherwise print an error message.
   */
  private void goRoom(Command command) {
    String direction = command.getSecondWord();

    // Try to leave current room.
    Exit exit = currentRoom.getExit(direction);
    if(!exit.canPass(this)) {
      for(CharacterListener listener: listeners) {
        listener.cantPass();
      }
      return;
    }
    Room nextRoom = exit.getRoom();
    if(nextRoom == null) {
      return;
    }

    timer.cancel();
    //A timer can't be canceled twice.
    //Create a new one.
    timer = new Timer();
    timer.schedule(new TimeoutTask(), 1000 * timePerRoom);
    currentRoom = nextRoom;
    for(CharacterListener listener: listeners) {
      listener.roomEntered(currentRoom);
      listener.exitPassed(direction, exit);
    }
  }

  /**
   * "Quit" was entered. Check the rest of the command to see
   * whether we really quit the game.
   */
  private void quit(Command command) {
    for(CharacterListener listener: listeners) {
      listener.quitGame();
    }
  }


  private class TimeoutTask extends TimerTask {
    @Override
    public void run() {
      for(CharacterListener listener: listeners) {
        listener.timeUp();
      }
      System.exit(0);
    }
  }
}