package hu.zagor.gamebooks.books.contentransforming.section.stub;

/**
 * FF-specific class for transforming the "market" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class FfMarketTransformer extends ComplexMarketTransformer {
    @Override
    protected String getDefaultMultipleCcyKey() {
        return "page.ff.label.market.goldPieces";
    }

    @Override
    protected String getDefaultSingleCcyKey() {
        return "page.ff.label.market.goldPiece";
    }

}
