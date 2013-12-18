package se.considerate.doors.command;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */
public class Parser implements Runnable
{
    private Scanner reader;         // source of command input
    private boolean running;
    private ArrayList<CommandListener> listeners = new ArrayList<CommandListener>();

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        reader = new Scanner(System.in);
        this.running = true;
    }

    public void addEventListener(CommandListener listener) {
        listeners.add(listener);
    }

    public void close() {
        this.running = false;
    }

    public Command parseCommand(String inputLine) {
        String word1 = null;
        String word2 = null;
        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();      // get first word
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();      // get second word
                // note: we just ignore the rest of the input line.
            }
        }
        Command command = new Command(word1, word2);;
        return command;
    }

    public void run() {
        while(this.running) {
            System.out.print("> ");
            String inputLine;   // will hold the full input line

            inputLine = reader.nextLine();
            Command command = parseCommand(inputLine);

            // Now check whether this word is known. If so, create a command
            // with it. If not, create a "null" command (for unknown command).
            if(command != null) {        
                for (CommandListener listener : listeners) {
                    listener.commandRecieved(command);
                }
            } 
        }
    }
}
