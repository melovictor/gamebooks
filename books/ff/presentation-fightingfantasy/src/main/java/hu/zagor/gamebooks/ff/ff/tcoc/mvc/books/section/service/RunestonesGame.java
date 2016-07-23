package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain.RunestoneMessageData;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Component;

/**
 * Service class for playing the game Runestones.
 * @author Tamas_Szekeres
 */
@Component
public class RunestonesGame {

    private static final int WINNING = 36;
    private static final int CHOICE_CONTINUE_ROUND = 1;
    private static final int CHOICE_RESTART_GAME = 2;
    private static final int CHOICE_START_GAME_SIX_PICK = 3;
    private static final int CHOICE_START_GAME_KNIFEY = 4;
    private static final int CHOICE_LEAVE_GAMBLING_ROOM = 5;

    private static final int STAMINA_DEDUCTION = -4;
    private static final int SKILL_DEDUCTION = -2;
    private static final int MAX_ROLLABLE = 12;
    private static final int LAST_CHAR_OF_ID = 4;

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator randomNumberGenerator;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;
    @Autowired
    private DiceResultRenderer diceResultRenderer;

    /**
     * Play a single round of the game Runestones.
     * @param character the {@link FfCharacter} object
     * @param characterHandler the {@link FfCharacterHandler} object
     * @param paragraph the {@link Paragraph} object
     */
    public void playRound(final FfCharacter character, final FfCharacterHandler characterHandler, final Paragraph paragraph) {
        final int numberOfPlayers = getNumberOfPlayers(character);
        playOneRound(numberOfPlayers, character, characterHandler, paragraph.getData());
    }

    private void playOneRound(final int numberOfPlayers, final FfCharacter character, final FfCharacterHandler characterHandler, final ParagraphData data) {
        int limit = MAX_ROLLABLE;
        int thrownValue = 0;
        final StringBuilder builder = new StringBuilder(messageSource.getMessage("page.ff2.runestones.opening.players" + numberOfPlayers, null,
            localeProvider.getLocale()));
        final RunestoneMessageData msgData = new RunestoneMessageData();
        msgData.setNumberOfPlayers(numberOfPlayers);

        do {
            final int playerNumber = (MAX_ROLLABLE - limit) % numberOfPlayers;
            msgData.setPlayerNumber(playerNumber);
            final int[] thrownValues = randomNumberGenerator.getRandomNumber(2);
            thrownValue = thrownValues[0];

            if (thrownValue == limit) {
                handleFinishingRoundWithoutFallouts(msgData);
            } else if (thrownValue > limit) {
                handleFinishingRound(character, characterHandler, msgData);
            } else {
                handleJumpingToNextPlayer(numberOfPlayers, playerNumber, msgData);
            }

            appendMessageToBuilder(thrownValue, builder, msgData, thrownValues);
        } while (thrownValue < limit--);

        if (msgData.isWon()) {
            builder.append("[p]" + messageSource.getMessage("page.ff2.runestones.win", null, localeProvider.getLocale()) + "[/p]");
            character.setGold(character.getGold() + WINNING);
        }

        data.setText(builder.toString());
        selectValidChoices(data, msgData);
    }

    private void selectValidChoices(final ParagraphData data, final RunestoneMessageData msgData) {
        if (msgData.isFinished()) {
            data.getChoices().removeByPosition(CHOICE_CONTINUE_ROUND);
        } else {
            data.getChoices().removeByPosition(CHOICE_RESTART_GAME);
            data.getChoices().removeByPosition(CHOICE_START_GAME_SIX_PICK);
            data.getChoices().removeByPosition(CHOICE_START_GAME_KNIFEY);
            data.getChoices().removeByPosition(CHOICE_LEAVE_GAMBLING_ROOM);
        }
    }

    private void appendMessageToBuilder(final int thrownValue, final StringBuilder builder, final RunestoneMessageData msgData, final int[] thrownValues) {
        final Locale locale = localeProvider.getLocale();
        final String diceRender = diceResultRenderer.render(randomNumberGenerator.getDefaultDiceSide(), thrownValues);
        final String diceRollText = messageSource.getMessage("page.ff.label.random.after", new Object[]{diceRender, thrownValue}, locale);
        final String resolvedText = messageSource.getMessage(msgData.getMessageKey(), new Object[]{msgData.getPlayerA(), msgData.getPlayerB()}, locale);
        builder.append("[p]" + diceRollText + "[br /]" + resolvedText + "[/p]");
    }

    private void handleFinishingRoundWithoutFallouts(final RunestoneMessageData msgData) {
        msgData.setMessageKey("page.ff2.runestones.rockBlewUpInAir");
    }

    private void handleFinishingRound(final FfCharacter character, final FfCharacterHandler characterHandler, final RunestoneMessageData msgData) {
        final int playerNumber = msgData.getPlayerNumber();
        if (playerNumber == 0) {
            msgData.setMessageKey("page.ff2.runestones.rockBlewUpInYourHand");
            character.changeStamina(STAMINA_DEDUCTION);
            character.changeSkill(SKILL_DEDUCTION);
            msgData.setFinished(true);
        } else {
            msgData.setPlayerA(messageSource.getMessage("page.ff2.runestones.player" + playerNumber, null, localeProvider.getLocale()));
            msgData.setMessageKey("page.ff2.runestones.rockBlewUpAtPlayer");
            final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
            final int numberOfPlayers = msgData.getNumberOfPlayers();
            itemHandler.removeItem(character, "4278" + numberOfPlayers, 1);
            itemHandler.addItem(character, "4278" + (numberOfPlayers - 1), 1);
            msgData.setFinished(numberOfPlayers == 2);
            msgData.setWon(msgData.isFinished());
        }
    }

    private void handleJumpingToNextPlayer(final int numberOfPlayers, final int playerNumber, final RunestoneMessageData msgData) {
        if (playerNumber == 0) {
            msgData.setMessageKey("page.ff2.runestones.passing.youToPlayer");
        } else {
            final Locale locale = localeProvider.getLocale();
            msgData.setPlayerA(messageSource.getMessage("page.ff2.runestones.player" + playerNumber, null, locale));
            if (playerNumber == numberOfPlayers - 1) {
                msgData.setMessageKey("page.ff2.runestones.passing.playerToYou");
            } else {
                msgData.setPlayerB(messageSource.getMessage("page.ff2.runestones.player" + (playerNumber + 1), null, locale));
                msgData.setMessageKey("page.ff2.runestones.passing.playerToPlayer");
            }
        }
    }

    private int getNumberOfPlayers(final FfCharacter character) {
        int numOfPlayers = 0;
        for (final Item item : character.getEquipment()) {
            if (item.getId().startsWith("4278")) {
                numOfPlayers = Integer.valueOf(item.getId().substring(LAST_CHAR_OF_ID));
            }
        }
        return numOfPlayers;
    }

}
