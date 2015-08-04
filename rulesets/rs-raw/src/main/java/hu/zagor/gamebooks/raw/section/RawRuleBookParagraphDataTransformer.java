package hu.zagor.gamebooks.raw.section;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractBookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

/**
 * Transformer that reads only the most basic paragraph elements from the contents, and doesn't support items
 * or enemies.
 * @author Tamas_Szekeres
 */
public class RawRuleBookParagraphDataTransformer extends AbstractBookParagraphDataTransformer {

    @Override
    protected ParagraphData getParagraphData(final ChoicePositionCounter positionCounter) {
        final ParagraphData paragraphData = getParagraphData();
        paragraphData.setPositionCounter(positionCounter);
        return paragraphData;
    }

    @Override
    public ParagraphData getParagraphData() {
        return getBeanFactory().getBean("paragraphData", ParagraphData.class);
    }

}
