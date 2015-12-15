package hu.zagor.gamebooks.content.command;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import java.util.Locale;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Base test class with text resolvation capabilities.
 * @author Tamas_Szekeres
 */
public abstract class CoreTextResolvingTest {

    private final Locale locale = Locale.ENGLISH;
    @Inject private HierarchicalMessageSource messageSource;
    @Inject private LocaleProvider localeProvider;

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
