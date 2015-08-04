package hu.zagor.gamebooks.content;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import java.util.Iterator;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ParagraphData}.
 * @author Tamas_Szekeres
 */
@Test
public class ParagraphDataCloneTest {

    private ParagraphData underTest;
    private ChoicePositionComparator choiceComparator;
    private Logger logger;
    private IMocksControl mockControl;
    private ChoiceSet choiceSet;
    private BeanFactory beanFactory;

    private GatheredLostItem itemA;
    private GatheredLostItem itemB;
    private GatheredLostItem itemC;
    private GatheredLostItem itemD;
    private GatheredLostItem mockedItemA;
    private GatheredLostItem mockedItemB;
    private GatheredLostItem mockedItemC;
    private GatheredLostItem mockedItemD;

    private Choice choiceA;
    private Choice choiceB;
    private Choice clonedChoiceA;
    private Choice clonedChoiceB;

    private Command commandA;
    private Command commandB;
    private Command clonedCommandA;
    private Command clonedCommandB;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        choiceComparator = mockControl.createMock(ChoicePositionComparator.class);
        choiceSet = mockControl.createMock(ChoiceSet.class);
        beanFactory = mockControl.createMock(BeanFactory.class);

        itemA = mockControl.createMock(GatheredLostItem.class);
        itemB = mockControl.createMock(GatheredLostItem.class);
        itemC = mockControl.createMock(GatheredLostItem.class);
        itemD = mockControl.createMock(GatheredLostItem.class);
        mockedItemA = mockControl.createMock(GatheredLostItem.class);
        mockedItemB = mockControl.createMock(GatheredLostItem.class);
        mockedItemC = mockControl.createMock(GatheredLostItem.class);
        mockedItemD = mockControl.createMock(GatheredLostItem.class);

        choiceA = mockControl.createMock(Choice.class);
        choiceB = mockControl.createMock(Choice.class);
        clonedChoiceA = mockControl.createMock(Choice.class);
        clonedChoiceB = mockControl.createMock(Choice.class);

        commandA = mockControl.createMock(Command.class);
        commandB = mockControl.createMock(Command.class);
        clonedCommandA = mockControl.createMock(Command.class);
        clonedCommandB = mockControl.createMock(Command.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ParagraphData();
        underTest.setText("");
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setBeanFactory(beanFactory);
        underTest.setChoices(new DefaultChoiceSet(choiceComparator));
        underTest.getChoices().clear();
        underTest.getCommands().clear();
        underTest.getGatheredItems().clear();
        underTest.getLostItems().clear();
        underTest.addCommand(commandA);
        underTest.addCommand(commandB);
        underTest.addGatheredItem(itemA);
        underTest.addGatheredItem(itemB);
        underTest.addLostItem(itemC);
        underTest.addLostItem(itemD);

        mockControl.reset();
    }

    public void testCloneShouldCreateClone() throws CloneNotSupportedException {
        // GIVEN
        expect(choiceComparator.compare(choiceA, choiceA)).andReturn(0);
        expect(choiceComparator.compare(choiceB, choiceA)).andReturn(1);

        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        expect(choiceA.clone()).andReturn(clonedChoiceA);
        expect(choiceSet.add(clonedChoiceA)).andReturn(true);
        expect(choiceB.clone()).andReturn(clonedChoiceB);
        expect(choiceSet.add(clonedChoiceB)).andReturn(true);

        expect(commandA.clone()).andReturn(clonedCommandA);
        expect(commandB.clone()).andReturn(clonedCommandB);

        expect(itemA.clone()).andReturn(mockedItemA);
        expect(itemB.clone()).andReturn(mockedItemB);
        expect(itemC.clone()).andReturn(mockedItemC);
        expect(itemD.clone()).andReturn(mockedItemD);

        mockControl.replay();
        // WHEN
        underTest.addChoice(choiceA);
        underTest.addChoice(choiceB);
        final ParagraphData returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned.getCommands(), underTest.getCommands());
        Assert.assertNotSame(returned.getChoices(), underTest.getChoices());
        Assert.assertNotSame(returned.getGatheredItems(), underTest.getGatheredItems());
        Assert.assertNotSame(returned.getLostItems(), underTest.getLostItems());
        Assert.assertEquals(returned.getCommands().size(), underTest.getCommands().size());
        final Iterator<Command> returnedCommandIterator = returned.getCommands().iterator();
        Assert.assertEquals(returnedCommandIterator.next(), clonedCommandA);
        Assert.assertEquals(returnedCommandIterator.next(), clonedCommandB);
        Assert.assertEquals(returned.getGatheredItems().size(), 2);
        Assert.assertEquals(returned.getLostItems().size(), 2);
        Assert.assertTrue(returned.getGatheredItems().contains(mockedItemA));
        Assert.assertTrue(returned.getGatheredItems().contains(mockedItemB));
        Assert.assertTrue(returned.getLostItems().contains(mockedItemC));
        Assert.assertTrue(returned.getLostItems().contains(mockedItemD));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
