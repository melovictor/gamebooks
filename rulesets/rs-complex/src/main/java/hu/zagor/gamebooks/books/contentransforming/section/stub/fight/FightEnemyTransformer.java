package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import org.w3c.dom.Node;

/**
 * Transforms the enemy element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightEnemyTransformer extends AbstractCommandSubTransformer<ComplexFightCommand<Enemy>> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ComplexFightCommand<Enemy> command,
        final ChoicePositionCounter positionCounter) {
        final String id = extractAttribute(node, "id");
        command.getEnemies().add(id);
    }

}
