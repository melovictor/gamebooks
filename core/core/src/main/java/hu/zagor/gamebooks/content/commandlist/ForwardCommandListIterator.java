package hu.zagor.gamebooks.content.commandlist;

import hu.zagor.gamebooks.content.command.Command;

/**
 * A forward iterator for the command list.
 * @author Tamas_Szekeres
 */
public class ForwardCommandListIterator implements CommandListIterator {

    private final CommandList list;
    private int index;

    /**
     * Basic constructor that returns a new iterator with the list.
     * @param list the underlying list
     */
    public ForwardCommandListIterator(final CommandList list) {
        this.list = list;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return list.size() > index;
    }

    @Override
    public Command next() {
        return list.get(index++);
    }

    @Override
    public void remove() {
        list.remove(--index);
    }

    @Override
    public void add(final CommandList commands) {
        list.addAll(index, commands);
    }

}
