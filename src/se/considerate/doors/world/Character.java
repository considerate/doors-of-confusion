package se.considerate.doors.world;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.io.PrintStream;

import se.considerate.doors.command.Command;
import se.considerate.doors.command.CommandRunner;

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
        //TODO: implement inspect command
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
        //TODO: implement pickup command
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
   * If there is no available command with name of the given command
   * alert all listeners that we have an unknown command.
   * @param command The command to be processed.
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

  public void welcome()
  {
    Room firstRoom = currentRoom;
    for(CharacterListener listener: listeners) {
      listener.characterCreated(firstRoom);
    }
  }

  public void printHelp() {
    for(CharacterListener listener: listeners) {
      listener.helpRequested(commands);
    }
  }

  private void goRoom(Command command) {
    if(command == null) {
      return;
    }

    String direction = command.getSecondWord();

    // Try to leave current room.
    Exit exit = currentRoom.getExit(direction);
    System.out.println(direction);
    System.out.println(exit);
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
    //A timer can't be cancelled twice.
    //Create a new one.
    timer = new Timer();
    timer.schedule(new TimeoutTask(), 1000 * timePerRoom);
    currentRoom = nextRoom;
    for(CharacterListener listener: listeners) {
      listener.roomEntered(currentRoom);
      listener.exitPassed(direction, exit);
    }
  }

  private void quit(Command command) {
    if(command == null) {
      return;
    }
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