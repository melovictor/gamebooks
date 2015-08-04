package hu.zagor.gamebooks.controller.image;

/**
 * Enum for image lookup strategies.
 * @author Tamas_Szekeres
 */
public enum ImageLookupStrategyType {

    /**
     * First look for black and white images, then colored ones.
     */
    BW_COLOR("bwFirst"),
    /**
     * First look for colored images, then black and white ones.
     */
    COLOR_BW("colFirst");

    private String id;

    private ImageLookupStrategyType(final String id) {
        this.id = id;
    }

    /**
     * Returns an {@link ImageLookupStrategyType} based on the string it receives.
     * @param string the type requested
     * @return the appropriate enum
     */
    public static ImageLookupStrategyType fromConfig(final String string) {
        ImageLookupStrategyType result = BW_COLOR;
        for (final ImageLookupStrategyType type : ImageLookupStrategyType.values()) {
            if (type.id.equals(string)) {
                result = type;
            }
        }

        return result;
    }
}
