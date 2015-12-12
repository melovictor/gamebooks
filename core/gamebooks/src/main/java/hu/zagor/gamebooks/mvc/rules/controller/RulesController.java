package hu.zagor.gamebooks.mvc.rules.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.generic.controller.LanguageAwareController;
import hu.zagor.gamebooks.mvc.rules.domain.HelpDescriptor;
import hu.zagor.gamebooks.mvc.rules.domain.HelpSeriesBooks;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the rules page.
 * @author Tamas_Szekeres
 */
@Controller
public class RulesController extends LanguageAwareController {

    @LogInject private Logger logger;
    @Autowired private LocaleProvider localeProvider;

    /**
     * Handles the displaying of the main rules list with all available books sorted by series.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} object
     * @return the name of the tile to display
     */
    @RequestMapping(value = PageAddresses.RULES, method = RequestMethod.GET)
    public String displayRulesMainScreen(final Model model, final HttpServletRequest request) {
        final Set<HelpSeriesBooks> series = getHelps();
        initializeLanguages(model, request);
        model.addAttribute("pageTitle", "page.title");
        model.addAttribute("series", series);
        model.addAttribute("locale", localeProvider.getLocale().getLanguage());

        return "rules";
    }

    private Set<HelpSeriesBooks> getHelps() {
        final Set<HelpSeriesBooks> series = new TreeSet<HelpSeriesBooks>();

        for (final HelpDescriptor descriptor : fetchHelpDescriptors()) {
            addNewDescriptor(series, descriptor);
        }
        return series;
    }

    private void addNewDescriptor(final Set<HelpSeriesBooks> series, final HelpDescriptor descriptor) {
        final BookInformations info = descriptor.getInfo();
        final String seriesName = info.getSeries();
        final HelpSeriesBooks serie = getSeries(seriesName, series, info);
        serie.add(descriptor.getInfo());
    }

    private HelpSeriesBooks getSeries(final String seriesName, final Set<HelpSeriesBooks> series, final BookInformations info) {
        HelpSeriesBooks found = null;
        for (final HelpSeriesBooks helpSeriesBooks : series) {
            if (seriesName.equals(helpSeriesBooks.getSeriesTitle())) {
                found = helpSeriesBooks;
            }
        }
        if (found == null) {
            found = new HelpSeriesBooks();
            found.setLocale(info.getLocale().getLanguage());
            found.setSeriesTitle(seriesName);
            series.add(found);
        }
        return found;
    }

    /**
     * Handles the displaying of the main rules list with all available books sorted by series.
     * @param model the {@link Model} object
     * @param bookId the ID number of the book for which we want to see the rules
     * @return the name of the tile to display
     */
    @RequestMapping(value = PageAddresses.RULES + "/{bookId}", method = RequestMethod.GET)
    public String displayRulesSpecificScreen(final Model model, @PathVariable("bookId") final Long bookId) {
        model.addAttribute("pageTitle", "page.title");
        model.addAttribute("locale", localeProvider.getLocale().getLanguage());
        model.addAttribute("bookSpecificRules", true);

        for (final HelpDescriptor descriptor : fetchHelpDescriptors()) {
            if (bookId.equals(descriptor.getInfo().getId())) {
                model.addAttribute("helpDescriptor", descriptor);
                model.addAllAttributes(descriptor.getParams());
            }
        }

        return "bookRule";
    }

    private HelpDescriptor[] fetchHelpDescriptors() {
        return getApplicationContext().getBeansOfType(HelpDescriptor.class).values().toArray(new HelpDescriptor[]{});
    }

}
