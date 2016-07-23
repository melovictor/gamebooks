package hu.zagor.gamebooks.content.command.fight.roundresolver.domain;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.LwEnemy;
import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.lw.character.LwCharacter;

/**
 * Object for moving around fight-relevant beans.
 * @author Tamas_Szekeres
 */
public class FightDataDto {

    private final LwEnemy enemy;
    private final FightCommandMessageList messages;
    private final ResolvationData resolvationData;

    /**
     * Basic constructor.
     * @param enemy the current enemy
     * @param messages the list of messages that will be displayed on the page
     * @param resolvationData the {@link ResolvationData} bean
     */
    public FightDataDto(final LwEnemy enemy, final FightCommandMessageList messages, final ResolvationData resolvationData) {
        this.enemy = enemy;
        this.messages = messages;
        this.resolvationData = resolvationData;
    }

    public LwEnemy getEnemy() {
        return enemy;
    }

    public LwCharacter getCharacter() {
        return (LwCharacter) resolvationData.getCharacter();
    }

    public FightCommandMessageList getMessages() {
        return messages;
    }

    public LwAttributeHandler getAttributeHandler() {
        return getCharacterHandler().getAttributeHandler();
    }

    public LwCharacterHandler getCharacterHandler() {
        return (LwCharacterHandler) resolvationData.getCharacterHandler();
    }

}
