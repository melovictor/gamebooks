package hu.zagor.gamebooks.tm.section;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractBookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.TmParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

/**
 * Transformer for Time Machine books.
 * @author Tamas_Szekeres
 */
public class TmRuleBookParagraphDataTransformer extends AbstractBookParagraphDataTransformer {

    @Override
    protected ParagraphData getParagraphData(final ChoicePositionCounter positionCounter) {
        final TmParagraphData paragraphData = getParagraphData();
        paragraphData.setPositionCounter(positionCounter);
        return paragraphData;
    }

    @Override
    public TmParagraphData getParagraphData() {
        return getBeanFactory().getBean("tmParagraphData", TmParagraphData.class);
    }

}
