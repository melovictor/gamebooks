package hu.zagor.gamebooks.domain;

import org.springframework.util.Assert;

/**
 * Bean for containing a supported language.
 * @author Tamas_Szekeres
 */
public class SupportedLanguage {

    private final String localeCode;
    private final String countryCode;
    private final String selfName;

    /**
     * Basic constructor that creates a new {@link SupportedLanguage} bean with the provided locale code and the name of the language in it's own language.
     * @param localeCode the code for the given locale, cannot be null
     * @param countryCode the country code for the given locale, can be null
     * @param selfName the name of the language in it's own language, cannot be null
     */
    public SupportedLanguage(final String localeCode, final String countryCode, final String selfName) {
        Assert.notNull(localeCode, "Parameter 'localeCode' can not be null!");
        Assert.notNull(selfName, "Parameter 'selfName' can not be null!");
        this.localeCode = localeCode;
        this.selfName = selfName;
        this.countryCode = countryCode;
    }

    /**
     * Basic constructor that creates a new {@link SupportedLanguage} bean with the provided locale code and the name of the language in it's own language.
     * @param localeCode the code for the given locale, cannot be null
     * @param selfName the name of the language in it's own language, cannot be null
     */
    public SupportedLanguage(final String localeCode, final String selfName) {
        this(localeCode, null, selfName);
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public String getSelfName() {
        return selfName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getFlagCode() {
        return localeCode + (countryCode == null ? "" : "-" + countryCode.toLowerCase());
    }

    public String getLocaleFormat() {
        return localeCode + (countryCode == null ? "" : "_" + countryCode);
    }

}
