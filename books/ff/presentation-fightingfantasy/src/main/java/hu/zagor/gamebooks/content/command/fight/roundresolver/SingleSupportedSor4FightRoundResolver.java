package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.Sor4RedEyeRetaliationStrikeService;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single supported fight for Sor4.
 * @author Tamas_Szekeres
 */
@Component("singleSupportedsor4FightRoundResolver")
public class SingleSupportedSor4FightRoundResolver extends SingleSupportedFightRoundResolver implements BeanFactoryAware {
    private static final int ENEMY_ALLY_RED_EYE_SHIFT = 108 - 19;

    @Autowired private SorDamageReducingArmourService damageReducingArmourService;
    @Autowired @Qualifier("sorHeroAttackStrengthRoller") private HeroAttackStrengthRoller heroAttackStrengthRoller;
    @Resource(name = "sor4RedEyeAllies") private Set<String> redEyeAllies;
    @Autowired private Sor4RedEyeRetaliationStrikeService retaliationHandler;
    @Autowired private HttpServletRequest request;
    private BeanFactory beanFactory;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final Character character = resolvationData.getCharacter();
        final boolean redEyeAlly = isRedEyeAlly(character);
        if (redEyeAlly) {
            selectProperEnemy(resolvationData);
        }

        final FightRoundResult[] resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);

        if (redEyeAlly) {
            final FfAllyCharacter allyCharacter = (FfAllyCharacter) character;
            final String enemyId = getEnemyId(allyCharacter);
            final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get(enemyId);
            if (enemy.getStamina() <= 0) {
                allyCharacter.setStamina(0);
            }
        }

        return resolveRound;
    }

    @Override
    protected void damageSelf(final FightDataDto dto) {
        // TODO: in red eye-red eye fights, enemies must damage each other
        if (dto.getCharacter() instanceof SorCharacter && redEyeHasMirrorEnemy(dto)) {
            dto.getMessages().add("Hero missed red eye.");
        } else {
            damageReducingArmourService.setUpDamageProtection(dto);
            super.damageSelf(dto);
        }
    }

    private boolean redEyeHasMirrorEnemy(final FightDataDto dto) {
        final int enemyId = Integer.valueOf(dto.getEnemy().getId());
        final int mirrorId = enemyId + ENEMY_ALLY_RED_EYE_SHIFT;
        final FfEnemy ally = getEnemy(mirrorId);
        return ally.getStamina() > 0;
    }

    private FfEnemy getEnemy(final int mirrorId) {
        final HttpSessionWrapper wrapper = getWrapper();
        return (FfEnemy) wrapper.getEnemies().get(String.valueOf(mirrorId));
    }

    private HttpSessionWrapper getWrapper() {
        return (HttpSessionWrapper) beanFactory.getBean("httpSessionWrapper", request);
    }

    private void selectProperEnemy(final ResolvationData resolvationData) {
        final FfAllyCharacter character = (FfAllyCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final String enemyId = getEnemyId(character);

        characterHandler.getInteractionHandler().setFightCommand(character, LastFightCommand.ENEMY_ID, enemyId);
    }

    private String getEnemyId(final FfAllyCharacter character) {
        final int allyId = Integer.valueOf(getAllyId(character));
        return String.valueOf(allyId - ENEMY_ALLY_RED_EYE_SHIFT);
    }

    private boolean isRedEyeAlly(final Character character) {
        boolean isRedEye = false;
        if (character instanceof FfAllyCharacter) {
            isRedEye = redEyeAllies.contains(getAllyId(character));
        }
        return isRedEye;
    }

    private String getAllyId(final Character character) {
        final FfAllyCharacter allyCharacter = (FfAllyCharacter) character;
        final FfEnemy allyEnemy = allyCharacter.getAlly();
        return allyEnemy.getId();
    }

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        super.damageEnemy(command, dto);
        final FfEnemy enemy = dto.getEnemy();
        if (retaliationHandler.needToRetaliate(dto, enemy) && isHeroAttacker(dto)) {
            retaliationHandler.calculateRetaliationStrike(dto);
        }
    }

    private boolean isHeroAttacker(final FightDataDto dto) {
        return dto.getCharacter() instanceof SorCharacter;
    }

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        return heroAttackStrengthRoller.getSelfAttackStrength(character, command, attributeHandler);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
