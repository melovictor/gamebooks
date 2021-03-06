package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.character.handler.character.CharacterContinuator;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.UserInteractionHandler;

/**
 * Collector class for storing generic or custom handlers for a given book instance.
 * @author Tamas_Szekeres
 */
public class CharacterHandler {

    private CharacterItemHandler itemHandler;
    private CharacterParagraphHandler paragraphHandler;
    private CharacterGenerator characterGenerator;
    private CharacterContinuator characterContinuator;
    private UserInteractionHandler interactionHandler;
    private AttributeHandler attributeHandler;

    public CharacterItemHandler getItemHandler() {
        return itemHandler;
    }

    public void setItemHandler(final CharacterItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    public CharacterParagraphHandler getParagraphHandler() {
        return paragraphHandler;
    }

    public void setParagraphHandler(final CharacterParagraphHandler paragraphHandler) {
        this.paragraphHandler = paragraphHandler;
    }

    public CharacterGenerator getCharacterGenerator() {
        return characterGenerator;
    }

    public void setCharacterGenerator(final CharacterGenerator characterGenerator) {
        this.characterGenerator = characterGenerator;
    }

    public UserInteractionHandler getInteractionHandler() {
        return interactionHandler;
    }

    public void setInteractionHandler(final UserInteractionHandler interactionHandler) {
        this.interactionHandler = interactionHandler;
    }

    public AttributeHandler getAttributeHandler() {
        return attributeHandler;
    }

    public void setAttributeHandler(final AttributeHandler attributeHandler) {
        this.attributeHandler = attributeHandler;
    }

    public CharacterContinuator getCharacterContinuator() {
        return characterContinuator;
    }

    public void setCharacterContinuator(final CharacterContinuator characterContinuator) {
        this.characterContinuator = characterContinuator;
    }

}
