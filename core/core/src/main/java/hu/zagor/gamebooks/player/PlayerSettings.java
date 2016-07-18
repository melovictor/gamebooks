package hu.zagor.gamebooks.player;

import java.util.HashMap;

/**
 * Settings to store different configuration values for the user.
 */
public class PlayerSettings extends HashMap<String, String> {

    private static final String GLOBAL_DEFAULT_LANGUAGE = "global.defaultLanguage";
    private static final String DEV_INFORMATIVE_SECTIONS = "dev.informativeSections";
    private static final String GLOBAL_IMAGE_TYPE_ORDER = "global.imageTypeOrder";
    private static final String GLOBAL_SECTIONS_VISIBLE_CHOICE = "global.sectionsVisible.choice";
    private static final String GLOBAL_SECTIONS_VISIBLE_TOP = "global.sectionsVisible.top";
    private static final String SERIES_SORT_ORDER_PREFIX = "series.sortOrder.";

    public boolean isTopSectionDisplayable() {
        return Boolean.valueOf(get(GLOBAL_SECTIONS_VISIBLE_TOP));
    }

    public boolean isChoiceSectionDisplayable() {
        return Boolean.valueOf(get(GLOBAL_SECTIONS_VISIBLE_CHOICE));
    }

    public String getImageTypeOrder() {
        return get(GLOBAL_IMAGE_TYPE_ORDER);
    }

    public boolean isInformativeSectionsRequested() {
        return Boolean.valueOf(get(DEV_INFORMATIVE_SECTIONS));
    }

    /**
     * Setter for informativeSectionsRequested.
     * @param informativeSectionsRequested the informativeSectionsRequested
     */
    public void setInformativeSectionsRequested(final boolean informativeSectionsRequested) {
        put(DEV_INFORMATIVE_SECTIONS, String.valueOf(informativeSectionsRequested));
    }

    /**
     * Setter for choiceSectionDisplayable.
     * @param topSectionDisplayable the topSectionDisplayable
     */
    public void setTopSectionDisplayable(final boolean topSectionDisplayable) {
        put(GLOBAL_SECTIONS_VISIBLE_TOP, String.valueOf(topSectionDisplayable));
    }

    /**
     * Setter for choiceSectionDisplayable.
     * @param choiceSectionDisplayable the choiceSectionDisplayable
     */
    public void setChoiceSectionDisplayable(final boolean choiceSectionDisplayable) {
        put(GLOBAL_SECTIONS_VISIBLE_CHOICE, String.valueOf(choiceSectionDisplayable));
    }

    /**
     * Setter for imageTypeOrder.
     * @param imageTypeOrder the imageTypeOrder
     */
    public void setImageTypeOrder(final String imageTypeOrder) {
        put(GLOBAL_IMAGE_TYPE_ORDER, imageTypeOrder);
    }

    public String getDefaultLanguage() {
        return get(GLOBAL_DEFAULT_LANGUAGE);
    }

    /**
     * Setter for the default language.
     * @param language the language to set
     */
    public void setDefaultLanguage(final String language) {
        put(GLOBAL_DEFAULT_LANGUAGE, language);
    }

    /**
     * Returns the series sort order for a specific language.
     * @param language the language for which the sort order is requested
     * @return the stored sort order, can be null
     */
    public String getSeriesOrder(final String language) {
        return get(SERIES_SORT_ORDER_PREFIX + language);
    }

    /**
     * Saves a specific series sort order for the specified language.
     * @param language the language for which the sort order is saved
     * @param order the sort order
     */
    public void setSeriesOrder(final String language, final String order) {
        put(SERIES_SORT_ORDER_PREFIX + language, order);
    }

}
