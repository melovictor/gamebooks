package hu.zagor.gamebooks.ff.mvc.book.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic new game handling controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookNewGameController extends RawBookNewGameController {
    @Resource(name = "rewardIdAttribAssociations") private Map<String, String> rewardIdAssociations;

    @Override
    protected String doHandleNew(final HttpServletRequest request, final Model model, final Locale locale) {
        super.doHandleNew(request, model, locale);

        model.addAttribute("ffChoiceClass", "ffChoiceHidden");
        addJsResource(model, "ff");
        addCssResource(model, "ff");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Set<String> rewards = wrapper.getPlayer().getRewards().get(getInfo().getId());
        if (rewards != null) {
            model.addAttribute("earnedRewards", rewards.size());
            final ParagraphData data = wrapper.getParagraph().getData();
            String text = data.getText();

            final Map<String, Integer> rewardPrices = getRewardPrices();
            if (rewardPrices != null) {
                for (final Entry<String, String> entry : rewardIdAssociations.entrySet()) {
                    text = text.replace("id=\"" + entry.getKey() + "\"",
                        "id=\"" + entry.getKey() + "\" data-reward-price=\"" + rewardPrices.get(entry.getValue()) + "\" data-attrib-name=\"" + entry.getValue() + "\"");
                }
            }
            data.setText(text);
        }

        return "ffSection." + getInfo().getResourceDir();
    }

    @Override
    protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandlerObject) {
        super.setUpCharacterHandler(wrapper, characterHandlerObject);

        final FfCharacterHandler characterHandler = (FfCharacterHandler) characterHandlerObject;
        characterHandler.getEnemyHandler().setEnemies(wrapper.getEnemies());
    }

    @Override
    public FfCharacterPageData getCharacterPageData(final Character character) {
        return (FfCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    /**
     * Handles the generation of the new character.
     * @param request the {@link HttpServletRequest} object
     * @return the compiled object
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/" + PageAddresses.BOOK_GENERATE_CHARACTER, produces = "application/json")
    @ResponseBody
    public Map<String, Object> generateCharacter(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CharacterGenerator characterGenerator = getInfo().getCharacterHandler().getCharacterGenerator();
        final Map<String, Object> result = characterGenerator.generateCharacter(character, getInfo().getContentSpecification());

        initializeItems(request.getParameterMap(), character);

        return result;
    }

    /**
     * Handles the distribution of reward points.
     * @param request the {@link HttpServletRequest}
     * @param rewardDistribution the user's distribution of the reward points
     * @return the response object
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/distributeRewarPoints", produces = "application/json")
    @ResponseBody
    public Map<String, Object> distributeRewardPoints(final HttpServletRequest request, @RequestParam final Map<String, String> rewardDistribution) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerUser player = wrapper.getPlayer();
        if (validDistribution(player, rewardDistribution)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final FfAttributeHandler attributeHandler = getInfo().getCharacterHandler().getAttributeHandler();
            for (final Entry<String, String> entry : rewardDistribution.entrySet()) {
                final String[] keys = entry.getKey().split(",");
                for (final String key : keys) {
                    attributeHandler.handleModification(character, key, Integer.parseInt(entry.getValue()));
                }
            }
        }
        return new HashMap<>();
    }

    private boolean validDistribution(final PlayerUser player, final Map<String, String> rewardDistribution) {
        final long bookId = getInfo().getId();
        final Set<String> set = player.getRewards().get(bookId);
        final int totalRewards = set == null ? 0 : set.size();
        int chosenRewards = 0;
        final Map<String, Integer> rewardPrices = getRewardPrices();
        for (final Entry<String, String> entry : rewardDistribution.entrySet()) {
            final String key = entry.getKey();
            final Integer keyPrice = rewardPrices.get(key);
            if (keyPrice != null) {
                chosenRewards += keyPrice * Integer.parseInt(entry.getValue());
            }
        }

        return totalRewards >= chosenRewards;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> getRewardPrices() {
        Map<String, Integer> bean = null;
        final String beanName = getInfo().getResourceDir() + "RewardPrices";
        if (getApplicationContext().containsBean(beanName)) {
            bean = getBeanFactory().getBean(beanName, Map.class);
        }
        return bean;
    }

    /**
     * Method for processing the extra parameters received from the browser. The default implementation simply takes care of the potions.
     * @param parameterMap the parameters received
     * @param character the {@link FfCharacter} to initialize
     */
    protected void initializeItems(final Map<String, String[]> parameterMap, final FfCharacter character) {
        if (parameterMap.containsKey("potion")) {
            final String potionId = parameterMap.get("potion")[0];
            getInfo().getCharacterHandler().getItemHandler().addItem(character, potionId, 1);
        }
    }

    @Override
    protected BookParagraphConstants getStarterParagraph() {
        return BookParagraphConstants.GENERATE;
    }

    @Override
    public FfBookInformations getInfo() {
        return (FfBookInformations) super.getInfo();
    }

}
