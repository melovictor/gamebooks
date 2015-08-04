package hu.zagor.gamebooks.content.command;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.support.locale.LocaleProvider;

import java.util.Locale;

import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Base test class with text resolvation capabilities.
 * @author Tamas_Szekeres
 */
public abstract class CoreTextResolvingTest {

    private final Locale locale = Locale.ENGLISH;
    private HierarchicalMessageSource messageSource;
    private LocaleProvider localeProvider;

    public void init(final IMocksControl mockControl, final Object underTest) {
        if (messageSource == null) {
            messageSource = mockControl.createMock(HierarchicalMessageSource.class);
            localeProvider = mockControl.createMock(LocaleProvider.class);
        }
        Whitebox.setInternalState(underTest, "messageSource", messageSource);
        Whitebox.setInternalState(underTest, "localeProvider", localeProvider);
    }

    public void expectText(final String key, final Object[] params, final String response) {
        expectLocale();
        expectTextWoLocale(key, params, response);
    }

    public void expectTextWoLocale(final String key, final Object[] params, final String response) {
        expect(messageSource.getMessage(eq(key), aryEq(params), eq(locale))).andReturn(response);
    }

    public void expectLocale() {
        expect(localeProvider.getLocale()).andReturn(locale);
    }

    public Locale getLocale() {
        return locale;
    }
}
