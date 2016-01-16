package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.service;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.EnemyDependentFfBookPreFightHandlingService;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;

/**
 * Implementation of the {@link FfBookPreFightHandlingService} interface for the FF2 book.
 * @author Tamas_Szekeres
 */
public class Ff2BookPreFightHandlingService extends EnemyDependentFfBookPreFightHandlingService {

    private static final String DAGGER_ID = "3003";

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        if (DAGGER_ID.equals(itemId)) {
            damageEnemyWithDagger(info, wrapper);
        }
        return null;
    }

    private void damageEnemyWithDagger(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, DAGGER_ID, 1);
        final FfEnemy enemy = getEnemy(wrapper, info);
        enemy.setStamina(enemy.getStamina() - 2);
    }
}
