package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for transforming the "fight" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class FightTransformer extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<FightCommand>> fightTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        data.addCommand(parseFight(parent, node, data));
    }

    private Command parseFight(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final FightCommand fightCommand = getBeanFactory().getBean(FightCommand.class);

        fightCommand.setBattleType(extractAttribute(node, "type"));
        fightCommand.setResolver(extractAttribute(node, "resolver", "basic"));
        fightCommand.setForceOrder(extractBooleanAttribute(node, "forceOrder", false));
        fightCommand.setAttackStrengthRolledDices(extractIntegerAttribute(node, "attackStrengthRolledDices", 2));
        fightCommand.setAttackStrengthUsedDices(extractIntegerAttribute(node, "attackStrengthUsedDices", 2));
        fightCommand.setRecoverDamage(extractBooleanAttribute(node, "recoverDamage", false));
        fightCommand.setUsableWeaponTypes(toWeaponTypes(extractAttribute(node, "usableWeaponTypes",
            "shooting".equals(fightCommand.getBattleType()) ? "shooting" : "weapon1,weapon2").split(",")));
        final String forceWeapon = extractAttribute(node, "forceWeapon");
        if (!StringUtils.isEmpty(forceWeapon)) {
            final WeaponReplacementData replacementData = new WeaponReplacementData();
            replacementData.setForceWeapons(Arrays.asList(forceWeapon.split(",")));
            fightCommand.setReplacementData(replacementData);
        }

        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        final ChoicePositionCounter positionCounter = data.getPositionCounter();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                final String childNodeName = childNode.getNodeName();
                final CommandSubTransformer<FightCommand> responseTransformer = fightTransformers.get(childNodeName);
                if (responseTransformer == null) {
                    throw new UnsupportedOperationException(childNodeName);
                }
                responseTransformer.transform(parent, childNode, fightCommand, positionCounter);
            }
        }

        return fightCommand;
    }

    private List<ItemType> toWeaponTypes(final String[] weaponTypeNames) {
        final List<ItemType> usableWepaonTypes = new ArrayList<>();

        for (final String typeName : weaponTypeNames) {
            usableWepaonTypes.add(ItemType.valueOf(typeName));
        }

        return usableWepaonTypes;
    }

    public void setFightTransformers(final Map<String, CommandSubTransformer<FightCommand>> fightTransformers) {
        this.fightTransformers = fightTransformers;
    }

}
