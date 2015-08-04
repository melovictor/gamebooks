package hu.zagor.gamebooks.content.commandlist;

import hu.zagor.gamebooks.content.command.Command;

import java.util.ArrayList;

/**
 * A list for the commands with a proper list iterator that can handle looping forward while being able to add
 * or remove elements.
 * @author Tamas_Szekeres
 */
public class CommandList extends ArrayList<Command> {

    /**
     * Returns a forward iterator that can be used to add new elements to the underlying list as well.
     * @return the iterator
     */
    public CommandListIterator forwardsIterator() {
        return new ForwardCommandListIterator(this);
    }
}
