package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.enemy.FfEnemyHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;

/**
 * Fighting Fantasy-specific {@link CharacterHandler} object.
 * @author Tamas_Szekeres
 */
public class FfCharacterHandler extends CharacterHandler {

    private BattleLuckTestParameters battleLuckTestParameters;
    private FfEnemyHandler enemyHandler;
    private boolean canEatEverywhere = true;

    @Override
    public FfUserInteractionHandler getInteractionHandler() {
        return (FfUserInteractionHandler) super.getInteractionHandler();
    }

    @Override
    public FfCharacterItemHandler getItemHandler() {
        return (FfCharacterItemHandler) super.getItemHandler();
    }

    public BattleLuckTestParameters getBattleLuckTestParameters() {
        return battleLuckTestParameters;
    }

    public void setBattleLuckTestParameters(final BattleLuckTestParameters battleLuckTestParameters) {
        this.battleLuckTestParameters = battleLuckTestParameters;
    }

    @Override
    public FfAttributeHandler getAttributeHandler() {
        return (FfAttributeHandler) super.getAttributeHandler();
    }

    public boolean isCanEatEverywhere() {
        return canEatEverywhere;
    }

    public void setCanEatEverywhere(final boolean canEatEverywhere) {
        this.canEatEverywhere = canEatEverywhere;
    }

    public FfEnemyHandler getEnemyHandler() {
        return enemyHandler;
    }

    public void setEnemyHandler(final FfEnemyHandler enemyHandler) {
        this.enemyHandler = enemyHandler;
    }

}
