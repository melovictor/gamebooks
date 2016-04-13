package hu.zagor.gamebooks.content.command.fight.domain;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightCommandMessageList}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandMessageListTest {

    @MockControl private IMocksControl mockControl;
    private FightCommandMessageList underTest;

    @Mock private LocaleProvider localeProvider;
    @Mock private HierarchicalMessageSource messageSource;
    private final Locale locale = Locale.ENGLISH;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new FightCommandMessageList();
        Whitebox.setInternalState(underTest, "localeProvider", localeProvider);
        Whitebox.setInternalState(underTest, "messageSource", messageSource);
    }

    public void testConstructorWhenObjectIsCreatedShouldDefaultOnRoundMessages() {
        // GIVEN
        mockControl.replay();
        // WHEN
        // THEN
        Assert.assertSame(Whitebox.getInternalState(underTest, "selectedMessages"), Whitebox.getInternalState(underTest, "roundMessages"));
    }

    public void testSwitchToPreFightMessagesShouldSwitchToPreFightMessages() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.switchToPreFightMessages();
        // THEN
        Assert.assertSame(Whitebox.getInternalState(underTest, "selectedMessages"), Whitebox.getInternalState(underTest, "preFightMessages"));
    }

    public void testSwitchToPreRoundMessagesShouldSwitchToPreRoundMessages() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.switchToPreRoundMessages();
        // THEN
        Assert.assertSame(Whitebox.getInternalState(underTest, "selectedMessages"), Whitebox.getInternalState(underTest, "preRoundMessages"));
    }

    public void testSwitchToPostRoundMessagesShouldSwitchToPostRoundMessages() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.switchToPostRoundMessages();
        // THEN
        Assert.assertSame(Whitebox.getInternalState(underTest, "selectedMessages"), Whitebox.getInternalState(underTest, "postRoundMessages"));
    }

    public void testSwitchToRoundMessagesShouldSwitchToRoundMessages() {
        // GIVEN
        underTest.switchToPreRoundMessages();
        mockControl.replay();
        // WHEN
        underTest.switchToRoundMessages();
        // THEN
        Assert.assertSame(Whitebox.getInternalState(underTest, "selectedMessages"), Whitebox.getInternalState(underTest, "roundMessages"));
    }

    public void testSetRoundMessageWhenOnlyRoundNumberSpecifiedMustSetRoundMessageToDefault() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        // THEN
        Assert.assertEquals(Whitebox.getInternalState(underTest, "roundMessage"), "round 3");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testContainsShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.contains("something");
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testToArrayWhenTypeNotSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.toArray();
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testToArrayWhenTypeSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.toArray(new String[]{});
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testRemoveWhenObjectSpecifiedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.remove("something");
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testContainsAllShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.containsAll(new ArrayList<>());
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testAddAllWhenNoIndexSpecifiedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addAll(new ArrayList<String>());
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testAddAllWhenIndexSpecifiedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addAll(3, new ArrayList<String>());
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testRemoveAllShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeAll(new ArrayList<>());
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testRetainAllShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.retainAll(new ArrayList<>());
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetWhenIndexSpecifiedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.get(3);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.set(3, "something");
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testRemoveWhenIndexSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.remove(3);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testIndexOfShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.indexOf("something");
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testLastIndexOfShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.lastIndexOf("something");
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testListIteratorWhenNoIndexSpecifiedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.listIterator();
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testListIteratorWhenIndexSpecifiedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.listIterator(3);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSubListShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.subList(1, 5);
        // THEN throws exception
    }

    public void testGetLocaleWhenLocaleProviderIsSetShouldReturnLocale() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        mockControl.replay();
        // WHEN
        final Locale returned = underTest.getLocale();
        // THEN
        Assert.assertSame(returned, locale);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetLocaleWhenLocaleProviderIsNotSetShouldThrowException() {
        // GIVEN
        Whitebox.setInternalState(underTest, "localeProvider", (LocaleProvider) null);
        mockControl.replay();
        // WHEN
        underTest.getLocale();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSetRoundMessageWhenLocaleProviderIsNotSetShouldThrowException() {
        // GIVEN
        Whitebox.setInternalState(underTest, "localeProvider", (LocaleProvider) null);
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSetRoundMessageWhenMessageSourceIsNotSetShouldThrowException() {
        // GIVEN
        Whitebox.setInternalState(underTest, "messageSource", (MessageSource) null);
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddKeyWhenLocaleProviderIsNotSetShouldThrowException() {
        // GIVEN
        Whitebox.setInternalState(underTest, "localeProvider", (LocaleProvider) null);
        mockControl.replay();
        // WHEN
        underTest.addKey("new.message.key");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddKeyWhenMessageSourceIsNotSetShouldThrowException() {
        // GIVEN
        Whitebox.setInternalState(underTest, "messageSource", (MessageSource) null);
        mockControl.replay();
        // WHEN
        underTest.addKey("new.message.key");
        // THEN throws exception
    }

    public void testSizeWhenNoMessagesOrRoundMessageIsSetShouldReturnZero() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.size();
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testSizeWhenRoundMessageIsSetShouldReturnOne() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        final int returned = underTest.size();
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testSizeWhenPreFightMessageIsSetShouldReturnMessageCount() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        fillPreFight();
        final int returned = underTest.size();
        // THEN
        Assert.assertEquals(returned, 3);
    }

    public void testSizeWhenPreRoundMessageIsSetShouldReturnMessageCount() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        fillPreFight();
        fillPreRound();
        final int returned = underTest.size();
        // THEN
        Assert.assertEquals(returned, 4);
    }

    public void testSizeWhenRoundMessageIsSetShouldReturnMessageCount() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        underTest.setRoundMessage(3);
        fillPreFight();
        fillPreRound();
        fillRound();
        final int returned = underTest.size();
        // THEN
        Assert.assertEquals(returned, 8);
    }

    public void testSizeWhenPostRoundMessageIsSetShouldReturnMessageCount() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        fillMessages();
        final int returned = underTest.size();
        // THEN
        Assert.assertEquals(returned, 11);
    }

    public void testClearShouldClearAllMessages() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        fillMessages();
        underTest.clear();
        // THEN
        Assert.assertTrue(underTest.isEmpty());
    }

    public void testIsEmptyWhenEmptyShouldReturnTrue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isEmpty();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsEmptyWhenHasEntriesShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        fillPostRound();
        final boolean returned = underTest.isEmpty();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIteratorWhenRoundMessageIsSetShouldReturnIteratorThatReturnsAllElementsInOrder() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        fillMessages();
        final Iterator<String> returned = underTest.iterator();
        // THEN
        Assert.assertEquals(returned.next(), "text");
        Assert.assertEquals(returned.next(), "text2");
        Assert.assertEquals(returned.next(), "round 3");
        Assert.assertEquals(returned.next(), "preRMtxt");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "a");
        Assert.assertEquals(returned.next(), "b");
        Assert.assertEquals(returned.next(), "x");
    }

    public void testAddWhenIndexIsSetShouldInsertNewKeyAtProperLocation() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.fight.round"), aryEq(new Object[]{3}), eq(locale))).andReturn("round 3");
        mockControl.replay();
        // WHEN
        fillMessages();
        underTest.switchToRoundMessages();
        underTest.add(2, "something else");
        underTest.add("to the end");
        final Iterator<String> returned = underTest.iterator();
        // THEN
        Assert.assertEquals(returned.next(), "text");
        Assert.assertEquals(returned.next(), "text2");
        Assert.assertEquals(returned.next(), "round 3");
        Assert.assertEquals(returned.next(), "preRMtxt");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something else");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "to the end");
        Assert.assertEquals(returned.next(), "a");
        Assert.assertEquals(returned.next(), "b");
        Assert.assertEquals(returned.next(), "x");
    }

    public void testIteratorWhenNoRoundMessageIsSetShouldReturnIteratorThatReturnsAllElementsInOrder() {
        // GIVEN
        mockControl.replay();
        // WHEN
        fillPreFight();
        fillPreRound();
        fillRound();
        fillPostRound();
        final Iterator<String> returned = underTest.iterator();
        // THEN
        Assert.assertEquals(returned.next(), "text");
        Assert.assertEquals(returned.next(), "text2");
        Assert.assertEquals(returned.next(), "preRMtxt");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "a");
        Assert.assertEquals(returned.next(), "b");
        Assert.assertEquals(returned.next(), "x");
    }

    public void testPositionToShouldMoveCursorAfterSpecifiedElement() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("key.for.b"), aryEq(new String[]{}), eq(locale))).andReturn("b");
        mockControl.replay();
        // WHEN
        fillPreFight();
        fillPreRound();
        fillRound();
        fillPostRound();
        underTest.switchToPostRoundMessages();
        underTest.positionTo("key.for.b");
        underTest.add("k");
        final Iterator<String> returned = underTest.iterator();
        // THEN
        Assert.assertEquals(returned.next(), "text");
        Assert.assertEquals(returned.next(), "text2");
        Assert.assertEquals(returned.next(), "preRMtxt");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "a");
        Assert.assertEquals(returned.next(), "b");
        Assert.assertEquals(returned.next(), "k");
        Assert.assertEquals(returned.next(), "x");
    }

    public void testAddKeyShouldAddResolvedMessage() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("key.for.b"), aryEq(new String[]{}), eq(locale))).andReturn("b");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("key.for.k"), aryEq(new String[]{}), eq(locale))).andReturn("k");
        mockControl.replay();
        // WHEN
        fillPreFight();
        fillPreRound();
        fillRound();
        fillPostRound();
        underTest.switchToPostRoundMessages();
        underTest.positionTo("key.for.b");
        underTest.addKey("key.for.k");
        final Iterator<String> returned = underTest.iterator();
        // THEN
        Assert.assertEquals(returned.next(), "text");
        Assert.assertEquals(returned.next(), "text2");
        Assert.assertEquals(returned.next(), "preRMtxt");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "something more interesting");
        Assert.assertEquals(returned.next(), "a");
        Assert.assertEquals(returned.next(), "b");
        Assert.assertEquals(returned.next(), "k");
        Assert.assertEquals(returned.next(), "x");
    }

    private void fillMessages() {
        underTest.setRoundMessage(3);
        fillPreFight();
        fillPreRound();
        fillRound();
        fillPostRound();
    }

    private void fillPostRound() {
        underTest.switchToPostRoundMessages();
        underTest.add("a");
        underTest.add("b");
        underTest.add("x");
    }

    private void fillRound() {
        underTest.switchToRoundMessages();
        underTest.add("something more interesting");
        underTest.add("something more interesting");
        underTest.add("something more interesting");
        underTest.add("something more interesting");
    }

    private void fillPreRound() {
        underTest.switchToPreRoundMessages();
        underTest.add("preRMtxt");
    }

    private void fillPreFight() {
        underTest.switchToPreFightMessages();
        underTest.add("text");
        underTest.add("text2");
    }

}
