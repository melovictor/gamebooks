package hu.zagor.gamebooks.mvc.bookselection.domain.transformer;

import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.bookselection.domain.BookData;
import hu.zagor.gamebooks.mvc.bookselection.domain.SeriesCollection;
import hu.zagor.gamebooks.mvc.bookselection.domain.SeriesData;
import hu.zagor.gamebooks.player.PlayerUser;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link SeriesCollectionTransformer} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultSeriesCollectionTransformer implements SeriesCollectionTransformer {

    @Override
    public SeriesCollection createSeriesCollection(final List<BookInformations> bookList, final Locale locale, final PlayerUser player) {
        Assert.notNull(bookList, "Parameter 'bookList' can not be null!");
        Assert.notEmpty(bookList, "Parameter 'bookList' can not be empty!");
        Assert.notNull(locale, "Parameter 'locale' can not be null!");

        final SeriesCollection result = new SeriesCollection();

        for (final BookInformations bookInformations : bookList) {
            final Locale bookLocale = bookInformations.getLocale();
            if (bookLocale.getCountry() == locale.getCountry() && bookLocale.getLanguage() == locale.getLanguage()
                && (!bookInformations.isHidden() || player.isAdmin())) {
                addBookToCollection(result, bookInformations);
            }
        }

        return result;
    }

    private void addBookToCollection(final SeriesCollection result, final BookInformations bookInformations) {
        final String seriesName = bookInformations.getSeries();
        final BookData bookData = createBookData(bookInformations);
        provideSeries(result, seriesName);
        final SeriesData seriesData = result.get(seriesName);
        seriesData.addBook(bookData);
    }

    private BookData createBookData(final BookInformations bookInformations) {
        final BookData data = new BookData(bookInformations.getId());

        data.setTitle(bookInformations.getTitle());
        data.setPosition(bookInformations.getPosition());
        data.setSeries(bookInformations.getSeries());
        data.setDisabled(bookInformations.isDisabled() || bookInformations.isHidden());
        data.setUnfinished(bookInformations.isUnfinished());
        data.setLocale(bookInformations.getLocale());
        data.setResourceDir(bookInformations.getResourceDir());

        return data;
    }

    private void provideSeries(final SeriesCollection result, final String seriesName) {
        if (!result.containsKey(seriesName)) {
            final SeriesData seriesData = new SeriesData();
            seriesData.setName(seriesName);
            result.add(seriesData);
        }
    }

}
