package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;
import java.util.Map;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link ResolvationDataBuilder} interface.
 * @author Tamas_Szekeres
 */
public final class DefaultResolvationDataBuilder implements ResolvationDataBuilder {

    private final ResolvationData data;

    private DefaultResolvationDataBuilder() {
        data = new ResolvationData();
    }

    /**
     * Creates and returns a builder to use for building a {@link ResolvationData} object.
     * @return the builder
     */
    public static ResolvationDataBuilderParagraph builder() {
        return new DefaultResolvationDataBuilder();
    }

    @Override
    public ResolvationDataBuilderInfo withParagraph(final Paragraph paragraph) {
        Assert.notNull(paragraph);
        data.setRootData(paragraph.getData());
        data.setSection(paragraph.getId());
        return this;
    }

    @Override
    public ResolvationDataBuilderCharacter withBookInformations(final BookInformations info) {
        data.setInfo(info);
        return this;
    }

    @Override
    public ResolvationDataBuilderCharacter usingResolvationData(final ResolvationData resolvationData) {
        Assert.notNull(resolvationData);
        data.setRootData(resolvationData.getRootData());
        data.setEnemies(resolvationData.getEnemies());
        data.setInfo(resolvationData.getInfo());
        return this;
    }

    @Override
    public ResolvationDataBuilderPosition usingWrapper(final HttpSessionWrapper wrapper) {
        Assert.notNull(wrapper);
        data.setCharacter(wrapper.getCharacter());
        data.setEnemies(wrapper.getEnemies());
        data.setPlayerUser(wrapper.getPlayer());
        return this;
    }

    @Override
    public ResolvationDataBuilderTriplet withCharacter(final Character character) {
        data.setCharacter(character);
        return this;
    }

    @Override
    public ResolvationDataBuilderTriplet withEnemies(final Map<String, Enemy> enemies) {
        data.setEnemies(enemies);
        return this;
    }

    @Override
    public ResolvationDataBuilder withPlayer(final PlayerUser player) {
        data.setPlayerUser(player);
        return this;
    }

    @Override
    public ResolvationDataBuilderTriplet withPosition(final Integer position) {
        data.setPosition(position);
        return this;
    }

    @Override
    public ResolvationData build() {
        return data;
    }

}
