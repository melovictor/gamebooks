package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import org.w3c.dom.Node;

/**
 * Transforms the loseBattleRound element of a paragraph.
 * @author Tamas_Szekeres
 */
public class LoseBattleRoundTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        ((FfParagraphData) data).setLoseBattleRound(true);
    }

}
