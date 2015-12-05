package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;

/**
 * Class for transforming spell choices of a paragraph.
 * @author Tamas_Szekeres
 */
public class SpellTransformer extends ChoiceTransformer {
    @Override
    protected void addChoice(final ParagraphData data, final Choice choice) {
        ((SorParagraphData) data).addSpellChoice(choice);
    }
}
