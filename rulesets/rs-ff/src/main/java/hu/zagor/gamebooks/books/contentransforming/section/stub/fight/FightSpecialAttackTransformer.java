package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FfSpecialAttack;
import org.w3c.dom.Node;

/**
 * Transforms the specialAttack element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightSpecialAttackTransformer extends AbstractCommandSubTransformer<FfFightCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final FfFightCommand command, final ChoicePositionCounter positionCounter) {
        final FfSpecialAttack specialAttack = new FfSpecialAttack();

        specialAttack.setId(extractAttribute(node, "id"));
        specialAttack.setText(extractAttribute(node, "text"));

        command.getSpecialAttacks().add(specialAttack);
    }

}
