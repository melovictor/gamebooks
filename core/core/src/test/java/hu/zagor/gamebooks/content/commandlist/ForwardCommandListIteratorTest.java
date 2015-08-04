package hu.zagor.gamebooks.content.commandlist;

import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ForwardCommandListIterator}.
 * @author Tamas_Szekeres
 */
@Test
public class ForwardCommandListIteratorTest {

    public void testHasNextWhenListIsEmptyShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = new CommandList().forwardsIterator().hasNext();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasNextWhenListHasOneElementShouldReturnTrue() {
        // GIVEN
        final CommandList list = new CommandList();
        list.add(new UserInputCommand());
        // WHEN
        final boolean returned = list.forwardsIterator().hasNext();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testHasNextWhenListHasOneElementAndItHasBeenRequestedShouldReturnFalse() {
        // GIVEN
        final CommandList list = new CommandList();
        list.add(new UserInputCommand());
        final CommandListIterator iterator = list.forwardsIterator();
        iterator.next();
        // WHEN
        final boolean returned = iterator.hasNext();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testNextWhenListHasOneElementShouldReturnElement() {
        // GIVEN
        final CommandList list = new CommandList();
        final UserInputCommand command = new UserInputCommand();
        list.add(command);
        final CommandListIterator iterator = list.forwardsIterator();
        // WHEN
        final Command returned = iterator.next();
        // THEN
        Assert.assertSame(returned, command);
    }

    public void testHasNextWhenListHasOneElementWhichIsRemovedShouldReturnFalse() {
        // GIVEN
        final CommandList list = new CommandList();
        final UserInputCommand command = new UserInputCommand();
        list.add(command);
        final CommandListIterator iterator = list.forwardsIterator();
        iterator.next();
        iterator.remove();
        // WHEN
        final boolean returned = iterator.hasNext();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasNextWhenListHasTwoElementsAndFirstIsRemovedShouldReturnTrue() {
        // GIVEN
        final CommandList list = new CommandList();
        final UserInputCommand command = new UserInputCommand();
        list.add(new ItemCheckCommand());
        list.add(command);
        final CommandListIterator iterator = list.forwardsIterator();
        iterator.next();
        iterator.remove();
        // WHEN
        final boolean returned = iterator.hasNext();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testNextWhenListHasTwoElementsAndFirstIsRemovedShouldReturnSecond() {
        // GIVEN
        final CommandList list = new CommandList();
        final UserInputCommand command = new UserInputCommand();
        list.add(new ItemCheckCommand());
        list.add(command);
        final CommandListIterator iterator = list.forwardsIterator();
        iterator.next();
        iterator.remove();
        // WHEN
        final Command returned = iterator.next();
        // THEN
        Assert.assertSame(returned, command);
    }

    public void testHasNextWhenCommandListIsAddedToEmptyShouldReturnTrue() {
        // GIVEN
        final CommandList list = new CommandList();
        final CommandList additionalList = new CommandList();
        final UserInputCommand command = new UserInputCommand();
        additionalList.add(new ItemCheckCommand());
        additionalList.add(command);
        final CommandListIterator iterator = list.forwardsIterator();
        iterator.add(additionalList);
        // WHEN
        final boolean returned = iterator.hasNext();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testNextWhenCommandListIsAddedToEmptyShouldReturnFirstNewElement() {
        // GIVEN
        final CommandList list = new CommandList();
        final CommandList additionalList = new CommandList();
        final UserInputCommand command = new UserInputCommand();
        additionalList.add(command);
        additionalList.add(new ItemCheckCommand());
        final CommandListIterator iterator = list.forwardsIterator();
        iterator.add(additionalList);
        // WHEN
        final Command returned = iterator.next();
        // THEN
        Assert.assertSame(returned, command);
    }

}
