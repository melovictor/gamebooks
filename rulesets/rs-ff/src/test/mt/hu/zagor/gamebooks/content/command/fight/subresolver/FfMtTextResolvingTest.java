package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.Locale;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Base test class with text resolvation capabilities.
 * @author Tamas_Szekeres
 */
public abstract class FfMtTextResolvingTest {

    private final Locale locale = Locale.ENGLISH;
    private HierarchicalMessageSource messageSource;
    private LocaleProvider localeProvider;
    private FightCommandMessageList messageList;

    public void init(final IMocksControl mockControl, final Object underTest) {
        messageList = mockControl.createMock(FightCommandMessageList.class);
        try {
            Whitebox.getField(underTest.getClass(), "messageSource");
            if (messageSource == null) {
                messageSource = mockControl.createMock(HierarchicalMessageSource.class);
                localeProvider = mockControl.createMock(LocaleProvider.class);
            }
            Whitebox.setInternalState(underTest, "messageSource", messageSource);
            Whitebox.setInternalState(underTest, "localeProvider", localeProvider);
        } catch (final FieldNotFoundException ex) {
        }
    }

    public void init(final FightCommand command) {
        Whitebox.setInternalState(command, "messages", messageList);
    }

    public void expectText(final String key, final Object... params) {
        if (params == null) {
            expectText(key);
        } else {
            expect(messageList.addKey(key, params.length == 0 ? null : params)).andReturn(true);
        }
    }

    public void expectText(final String key) {
        expect(messageList.addKey(key)).andReturn(true);
    }

    public void expectTextWoLocale(final String key, final String response, final boolean shouldAddToMessageList, final Object... params) {
        expect(messageSource.getMessage(eq(key), aryEq(params.length == 0 ? null : params), eq(locale))).andReturn(response);
        if (shouldAddToMessageList) {
            expect(getMessageList().add(response)).andReturn(true);
        }
    }

    public void expectLocale() {
        expect(localeProvider.getLocale()).andReturn(locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public FightCommandMessageList getMessageList() {
        return messageList;
    }

    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

}
