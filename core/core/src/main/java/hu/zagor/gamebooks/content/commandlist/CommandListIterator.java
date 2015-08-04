package hu.zagor.gamebooks.content.commandlist;

import hu.zagor.gamebooks.content.command.Command;

import java.util.Iterator;

/**
 * Interface for iterating over the command lists.
 * @author Tamas_Szekeres
 */
public interface CommandListIterator extends Iterator<Command> {

    /**
     * Adds a list of commands to the underlying list from the current position.
     * @param commands the commands to add
     */
    void add(CommandList commands);

}
