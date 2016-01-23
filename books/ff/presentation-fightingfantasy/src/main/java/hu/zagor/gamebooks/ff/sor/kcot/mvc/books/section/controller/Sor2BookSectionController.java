package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.SorBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.KHARE_CITYPORT_OF_TRAPS)
public class Sor2BookSectionController extends SorBookSectionController {
    private static final int OGRE_WIN_PRICE_RATIO = 3;
    private static final int SELF_ENEMY_INITIAL_STAMINA = 99;
    private static final String SELF_ENEMY_ID = "9";
    private static final String FIGHT_WITH_SELF_ID = "67";
    private static final String DANCER_SECTION_ID = "17";
    private static final String MET_FLANKER_MARKER = "4013";
    @Resource(name = "flankerVisitTargets") private Map<String, String> flankerVisitTargets;
    @Autowired private LocaleProvider localeProvider;
    @Autowired private HierarchicalMessageSource source;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor2BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        super.handleAfterFight(wrapper, enemyId);
        final Character character = wrapper.getCharacter();
        if (character.getCommandView() == null) {
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, "4015", 1);
            itemHandler.removeItem(character, "4016", 1);
        }
    }

    /**
     * Handles the betting in the festival fight against the Ogre and the Barbarian.
     * @param request the {@link HttpServletRequest} object
     * @param id the id of the participant we're betting for
     * @param amount the amount we bet
     * @return the text to replace the contents of the character page with
     */
    @RequestMapping(value = "makeBet", method = RequestMethod.GET)
    @ResponseBody
    public String handleFightBetting(final HttpServletRequest request, @RequestParam("participant") final String id, @RequestParam("value") final int amount) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final int currentGold = character.getGold();
        final int bettedAmount = Math.min(amount, currentGold);
        character.setGold(currentGold - bettedAmount);
        getInfo().getCharacterHandler().getItemHandler().addItem(character, id, bettedAmount);

        final String message = source.getMessage("page.sor2.betOn" + id, new Object[]{bettedAmount}, localeProvider.getLocale());
        final ParagraphData data = wrapper.getParagraph().getData();
        data.setText(data.getText().replaceFirst("<div class=\"betting\">[^|]*<\\/button>", "<div class=\"betting\">" + message));

        return message;
    }

    /**
     * Handles the jumps for visiting Flanker, the assassin.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} object
     * @return the book page's name
     */
    @RequestMapping(value = "visitFlanker", method = RequestMethod.GET)
    public String handleFlankerJumps(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        final Paragraph oldParagraph = wrapper.getParagraph();
        final String currentSectionId = oldParagraph.getId();

        final Character character = wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (!itemHandler.hasItem(character, MET_FLANKER_MARKER)) {
            throw new InvalidStepChoiceException("You cannot meet Flanker if you never met him among the Shamutanti Hills.", currentSectionId);
        }
        final String targetSectionId = flankerVisitTargets.get(currentSectionId);

        if (targetSectionId == null) {
            throw new InvalidStepChoiceException("You cannot meet Flanker at your current position.", currentSectionId);
        }

        final ChoiceSet choices = oldParagraph.getData().getChoices();
        final Choice flanker = new Choice(targetSectionId, "", 99, null);
        choices.add(flanker);
        oldParagraph.addValidMove(targetSectionId);

        return handleSection(model, request, "s-" + targetSectionId);
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        super.handleCustomSectionsPre(model, wrapper, sectionIdentifier, paragraph);
        final String sectionId = paragraph.getId();
        if (DANCER_SECTION_ID.equals(sectionId)) {
            final List<ProcessableItemHolder> itemsToProcess = paragraph.getItemsToProcess();
            if (!itemsToProcess.isEmpty()) {
                final AttributeTestCommand command = (AttributeTestCommand) itemsToProcess.get(0).getCommand();
                final int itemCount = getItemCount((SorCharacter) wrapper.getCharacter());
                command.setAgainst(String.valueOf(itemCount));
            }
        } else if ("426".equals(sectionId)) {
            final ItemCheckCommand itemCheckCommand = (ItemCheckCommand) paragraph.getData().getCommands().get(0);
            final UserInputCommand userInputCommand = (UserInputCommand) itemCheckCommand.getHave().getCommands().get(0);
            final int goblinTeethCount = getInfo().getCharacterHandler().getItemHandler().getItems(wrapper.getCharacter(), "3201").size();
            userInputCommand.setMax(goblinTeethCount);
        }
    }

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        super.handleCustomSectionsPost(model, wrapper, sectionIdentifier, paragraph);
        if (FIGHT_WITH_SELF_ID.equals(paragraph.getId())) {
            final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get(SELF_ENEMY_ID);
            if (enemy.getStamina() == SELF_ENEMY_INITIAL_STAMINA) {
                final SorCharacter character = (SorCharacter) wrapper.getCharacter();
                enemy.setStamina(character.getStamina());
                enemy.setSkill(character.getSkill());
            }
        } else if ("234b".equals(paragraph.getId())) {
            setUpWinnings(wrapper);
        } else if ("448".equals(paragraph.getId()) || "506".equals(paragraph.getId())) {
            listHiddenItems(wrapper);
        }
    }

    private void listHiddenItems(final HttpSessionWrapper wrapper) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final Paragraph paragraph = wrapper.getParagraph();
        final ParagraphData data = paragraph.getData();
        final StringBuilder builder = new StringBuilder();

        final Set<String> addedItems = new HashSet<>();
        final List<Item> hiddenEquipment = character.getHiddenEquipment();
        for (final Item item : hiddenEquipment) {
            if (!addedItems.isEmpty()) {
                builder.append(", ");
            }
            addedItems.add(item.getId());
            builder.append("[span class=\"takeItem\" data-id=\"" + item.getId() + "\" data-amount=\"1\" data-group=\"storage\"]" + item.getName() + "[/span]");
        }

        data.setText(data.getText().replace("XXX", builder.toString()));
        paragraph.calculateValidEvents();
    }

    private void setUpWinnings(final HttpSessionWrapper wrapper) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();

        final boolean betForOgre = itemHandler.hasItem(character, "4025");
        final int betAmount = betForOgre ? itemHandler.getItems(character, "4025").size() : itemHandler.getItems(character, "4026").size();
        int winningAmount = 0;
        final FfEnemy ogre = (FfEnemy) wrapper.getEnemies().get("21");
        final boolean winnerIsOgre = ogre.getStamina() > 0;

        if (betForOgre && winnerIsOgre) {
            winningAmount = betAmount + betAmount / OGRE_WIN_PRICE_RATIO;
        } else if (!betForOgre && !winnerIsOgre) {
            winningAmount = 2 * betAmount;
        }

        if (winningAmount > 0) {
            character.setGold(character.getGold() + winningAmount);
            final ParagraphData data = wrapper.getParagraph().getData();
            data.setText(data.getText().replace("XX", String.valueOf(winningAmount)));
        }
    }

    private int getItemCount(final SorCharacter character) {
        int count = 0;
        if (character.getGold() > 0) {
            count = 1;
        }
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "2000")) {
            count++;
        }
        for (final Item item : character.getEquipment()) {
            if (item.getItemType() == ItemType.shadow) {
                continue;
            }
            if (item.getItemType() == ItemType.provision) {
                if (!"2000".equals(item.getId())) {
                    count++;
                }
            } else if (!"defWpn".equals(item.getId())) {
                count++;
            }
        }

        return count;
    }
}
