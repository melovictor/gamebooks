package hu.zagor.gamebooks.support.locale;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link LocaleProvider} interface that grabs the locale from the current
 * threadlocal using spring's automatic mechanisms.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultLocaleProvider implements LocaleProvider {

    @Override
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

}
