package hu.zagor.gamebooks.books.contentransforming.section.stub;

/**
 * LW-specific class for transforming the "market" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class LwMarketTransformer extends ComplexMarketTransformer {

    @Override
    protected String getDefaultMultipleCcyKey() {
        return "page.lw.label.market.goldCrowns";
    }

    @Override
    protected String getDefaultSingleCcyKey() {
        return "page.lw.label.market.goldCrown";
    }
}
