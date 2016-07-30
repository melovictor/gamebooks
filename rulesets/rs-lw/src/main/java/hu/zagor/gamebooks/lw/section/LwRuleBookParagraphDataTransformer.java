package hu.zagor.gamebooks.lw.section;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractBookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.lw.content.LwParagraphData;

/**
 * Transformer for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwRuleBookParagraphDataTransformer extends AbstractBookParagraphDataTransformer {

    @Override
    protected ParagraphData getParagraphData(final ChoicePositionCounter positionCounter) {
        final LwParagraphData paragraphData = getParagraphData();
        paragraphData.setPositionCounter(positionCounter);
        return paragraphData;
    }

    @Override
    public LwParagraphData getParagraphData() {
        return getBeanFactory().getBean("lwParagraphData", LwParagraphData.class);
    }

}
