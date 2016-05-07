package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import org.springframework.stereotype.Component;

/**
 * Handles the Snakes for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class SnakeHandler extends Ff60BeforeAfterRoundEnemyHandler {

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final Ff60Character character = (Ff60Character) resolvationData.getCharacter();
        character.setScarachnaPoison(character.getScarachnaPoison() + 2);
    }

    @Override
    protected String getEnemyId() {
        return "92";
    }

}
