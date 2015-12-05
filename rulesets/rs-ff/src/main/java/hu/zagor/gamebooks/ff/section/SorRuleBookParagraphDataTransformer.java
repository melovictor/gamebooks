package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;

/**
 * Transformer for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class SorRuleBookParagraphDataTransformer extends FfRuleBookParagraphDataTransformer {

    @Override
    public FfParagraphData getParagraphData() {
        return getBeanFactory().getBean("sorParagraphData", SorParagraphData.class);
    }

}
