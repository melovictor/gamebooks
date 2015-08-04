package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import org.w3c.dom.Node;

/**
 * Transforms the enemy element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightEnemyTransformer extends AbstractCommandSubTransformer<FightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final FightCommand command,
            final ChoicePositionCounter positionCounter) {
        final String id = extractAttribute(node, "id");
        command.getEnemies().add(id);
    }

}
