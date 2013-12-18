package se.considerate.doors.world;

import java.util.HashMap;
import se.considerate.doors.command.CommandRunner;

public interface CharacterListener {
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