package hu.zagor.gamebooks.mvc.generic.controller;

import hu.zagor.gamebooks.domain.SupportedLanguage;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

/**
 * Abstract class that is aware of the language choices and can initialize the model with such information.
 * @author Tamas_Szekeres
 */
public abstract class LanguageAwareController extends AbstractRequestWrappingController {

    @Autowired
    private SupportedLanguage[] availableLanguages;

    /**
     * Initializes the model with the information required to render the language switching widget.
     * @param model the model to populate
     * @param request the request
     */
    protected void initializeLanguages(final Model model, final HttpServletRequest request) {
        model.addAttribute("availableLanguages", availableLanguages);
        model.addAttribute("selectedLanguage", getLanguageCookie(request));
    }

    private String getLanguageCookie(final HttpServletRequest request) {
        String currentLanguage = null;
        for (final Cookie cookie : request.getCookies()) {
            if ("lang".equals(cookie.getName())) {
                currentLanguage = cookie.getValue();
            }
        }
        return currentLanguage;
    }
}
