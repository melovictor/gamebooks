package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.ComplexParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import org.w3c.dom.Node;

/**
 * Transforms the lose element inside the fight section.
 * @author Tamas_Szekeres
 * @param <E> the actual enemy type
 */
public class FightLoseTransformer<E extends Enemy> extends AbstractCommandSubTransformer<ComplexFightCommand<E>> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ComplexFightCommand<E> command,
        final ChoicePositionCounter positionCounter) {
        final ComplexParagraphData paragraphData = (ComplexParagraphData) parent.parseParagraphData(positionCounter, node);
        command.setLose(paragraphData);
    }
}
