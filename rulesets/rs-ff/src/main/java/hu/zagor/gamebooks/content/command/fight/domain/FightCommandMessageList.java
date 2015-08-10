package hu.zagor.gamebooks.content.command.fight.domain;

import hu.zagor.gamebooks.support.locale.LocaleProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Class for gathering the fight messages on the different levels.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class FightCommandMessageList implements List<String> {

    private int index;

    private final List<String> preFightMessages = new ArrayList<>();
    private String roundMessage;
    private final List<String> preRoundMessages = new ArrayList<>();
    private final List<String> roundMessages = new ArrayList<>();
    private final List<String> postRoundMessages = new ArrayList<>();

    private List<String> selectedMessages;

    @Autowired
    private LocaleProvider localeProvider;
    @Autowired
    private HierarchicalMessageSource messageSource;

    /**
     * Creates a new {@link FightCommandMessageList} object, defaulting on the round messages.
     */
    public FightCommandMessageList() {
        switchToRoundMessages();
    }

    /**
     * Switches to the pre-fight message stack.
     */
    public void switchToPreFightMessages() {
        selectedMessages = preFightMessages;
        index = selectedMessages.size();
    }

    /**
     * Sets the message responsible for reporting the round number.
     * @param key the message key
     * @param args the arguments
     */
    public void setRoundMessage(final String key, final Object[] args) {
        Assert.state(localeProvider != null, "A locale provider has to be specified for text key resolvation.");
        Assert.state(messageSource != null, "A message source has to be specified for text key resolvation.");
        final Locale locale = localeProvider.getLocale();
        index = 0;
        roundMessage = messageSource.getMessage(key, args, locale);
    }

    /**
     * Sets the default message responsible for reporting the round number.
     * @param roundNumber the number of the current round
     */
    public void setRoundMessage(final int roundNumber) {
        setRoundMessage("page.ff.label.fight.round", new Object[]{roundNumber});
    }

    /**
     * Switches to the pre-round message stack.
     */
    public void switchToPreRoundMessages() {
        selectedMessages = preRoundMessages;
        index = selectedMessages.size();
    }

    /**
     * Switches to the main, round message stack.
     */
    public void switchToRoundMessages() {
        selectedMessages = roundMessages;
        index = selectedMessages.size();
    }

    /**
     * Switches to the post round message stack.
     */
    public void switchToPostRoundMessages() {
        selectedMessages = postRoundMessages;
        index = selectedMessages.size();
    }

    @Override
    public int size() {
        int totalSize = roundMessage == null ? 0 : 1;
        totalSize += postRoundMessages.size();
        totalSize += preFightMessages.size();
        totalSize += preRoundMessages.size();
        totalSize += roundMessages.size();
        return totalSize;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> iterator() {
        final List<String> messages = new ArrayList<>();

        messages.addAll(preFightMessages);
        if (roundMessage != null) {
            messages.add(roundMessage);
        }
        messages.addAll(preRoundMessages);
        messages.addAll(roundMessages);
        messages.addAll(postRoundMessages);

        return messages.iterator();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(final String e) {
        ensureIndexCorrectness();
        selectedMessages.add(index, e);
        return true;
    }

    private void ensureIndexCorrectness() {
        if (index > selectedMessages.size()) {
            index = selectedMessages.size();
        }
    }

    /**
     * Positions the inner index of the currently selected message batch.
     * @param key the key of the text resource
     * @param args possible arguments for the message source
     */
    public void positionTo(final String key, final Object... args) {
        final Locale locale = localeProvider.getLocale();
        final String resolved = messageSource.getMessage(key, args, locale);
        index = selectedMessages.indexOf(resolved) + 1;
    }

    /**
     * Adds a text key with arguments that needs to be resolved.
     * @param key the key to be resolved
     * @param args the arguments
     * @return <code>true</code>
     */
    public boolean addKey(final String key, final Object... args) {
        Assert.state(localeProvider != null, "A locale provider has to be specified for text key resolvation.");
        Assert.state(messageSource != null, "A message source has to be specified for text key resolvation.");
        final Locale locale = localeProvider.getLocale();
        ensureIndexCorrectness();
        selectedMessages.add(index++, messageSource.getMessage(key, args, locale));
        return true;
    }

    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        preFightMessages.clear();
        preRoundMessages.clear();
        postRoundMessages.clear();
        roundMessages.clear();
        roundMessage = null;
    }

    @Override
    public String get(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(final int index, final String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(final int index, final String element) {
        selectedMessages.add(index, element);
    }

    @Override
    public String remove(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<String> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<String> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(final int fromIndex, final int toIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current locale.
     * @return the {@link Locale}
     */
    public Locale getLocale() {
        Assert.state(localeProvider != null, "A locale provider has to be specified for text key resolvation.");
        return localeProvider.getLocale();
    }

}
