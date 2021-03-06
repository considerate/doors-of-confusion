package se.considerate.doors.command;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Command
{
    private String commandWord = null;
    private String secondWord = null;
    private final String[] words;

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     */
    public Command(String[] words)
    {
        System.out.println(words.length);
        if(words.length >= 1) {
            this.commandWord = words[0];
        }
        if(words.length >= 2) {
            this.secondWord = words[1];
        }
        this.words = words;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord()
    {
        return secondWord;
    }

    public String[] getWords() {
        return this.words;
    }

    /**
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord()
    {
        return (secondWord != null);
    }

    public String toString() {
        if(this.commandWord == null) {
            return "";
        }
        if(this.secondWord == null) {
            return this.commandWord;
        }
        return this.commandWord + " " + this.secondWord;
    }
}

