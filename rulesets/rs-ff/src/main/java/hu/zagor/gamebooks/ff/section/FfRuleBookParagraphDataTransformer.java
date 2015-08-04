package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractBookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

/**
 * Transformer for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfRuleBookParagraphDataTransformer extends AbstractBookParagraphDataTransformer {

    @Override
    protected ParagraphData getParagraphData(final ChoicePositionCounter positionCounter) {
        final FfParagraphData paragraphData = getParagraphData();
        paragraphData.setPositionCounter(positionCounter);
        return paragraphData;
    }

    @Override
    public FfParagraphData getParagraphData() {
        return getBeanFactory().getBean("ffParagraphData", FfParagraphData.class);
    }

}
