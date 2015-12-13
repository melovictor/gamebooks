package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestSuccessType;
import hu.zagor.gamebooks.content.commandlist.CommandListIterator;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.raw.section.RawRuleBookParagraphResolver;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link BookParagraphResolver} for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfRuleBookParagraphResolver extends RawRuleBookParagraphResolver {

    private Map<String, AttributeTestSuccessType> attributeTestDefaultSuccessTypes;

    @Override
    protected void resolveCommands(final ResolvationData resolvationData, final ParagraphData genericSubData, final CommandListIterator mainCommandIterator) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfParagraphData subData = (FfParagraphData) genericSubData;

        handleAttributeModifiers(character, characterHandler, subData);

        executeImmediateCommands(resolvationData, subData.getImmediateCommands().forwardsIterator());

        super.resolveCommands(resolvationData, genericSubData, mainCommandIterator);
    }

    void handleAttributeModifiers(final FfCharacter character, final FfCharacterHandler characterHandler, final FfParagraphData subData) {
        final List<ModifyAttribute> modifyAttributes = subData.getModifyAttributes();
        for (final ModifyAttribute modAttr : modifyAttributes) {
            characterHandler.getAttributeHandler().handleModification(character, modAttr);
        }
        modifyAttributes.clear();
    }

    private void executeImmediateCommands(final ResolvationData resolvationData, final CommandListIterator commandIterator) {
        while (commandIterator.hasNext()) {
            final Command command = commandIterator.next();
            getLogger().debug("Executing command {}.", command.toString());
            final CommandResolver resolver = resolvationData.getInfo().getCommandResolvers().get(command.getClass());
            resolver.resolve(command, resolvationData);
        }
    }

    @Override
    protected void loseItems(final ParagraphData subData, final Character character, final CharacterItemHandler itemHandlerObject) {
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) itemHandlerObject;
        for (final GatheredLostItem item : subData.getLostItems()) {
            getLogger().debug("Lost item {}", item.getId());
            if ("equippedWeapon".equals(item.getId())) {
                final FfItem equippedWeapon = itemHandler.getEquippedWeapon((FfCharacter) character);
                itemHandler.removeItem(character, equippedWeapon.getId(), 1);
            } else {
                if (item.getDose() > 0) {
                    final FfItem toLose = (FfItem) itemHandler.getItem(character, item.getId());
                    if (toLose != null) {
                        if (toLose.getItemType() != ItemType.potion) {
                            throw new IllegalArgumentException("Trying to lose '" + item.getDose() + "' doses from non-potion item '" + toLose.getName() + "'.");
                        }
                        if (toLose.getDose() > item.getDose()) {
                            toLose.setDose(toLose.getDose() - item.getDose());
                        } else {
                            itemHandler.removeItem(character, item.getId(), 1);
                        }
                    }
                } else {
                    itemHandler.removeItem(character, item);
                }
            }
        }
        subData.getLostItems().clear();
    }

    /**
     * Returns the default success type specified for the current book for a specific test.
     * @param attributeTestType the type of the test (most usually skill or luck)
     * @return the default {@link AttributeTestSuccessType}
     */
    public AttributeTestSuccessType getAttributeTestDefaultSuccessType(final String attributeTestType) {
        return attributeTestDefaultSuccessTypes.get(attributeTestType);
    }

    public void setAttributeTestDefaultSuccessTypes(final Map<String, AttributeTestSuccessType> attributeTestDefaultSuccessTypes) {
        this.attributeTestDefaultSuccessTypes = attributeTestDefaultSuccessTypes;
    }

    @Override
    public String getRulesetPrefix() {
        return "ff";
    }
}
