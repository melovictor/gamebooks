package hu.zagor.gamebooks.content.command.market;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Main bean for storing data about a market opportunity.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class MarketCommand extends Command {

    private String singleCcyKey;
    private String multipleCcyKey;
    private List<MarketElement> itemsForSale = new ArrayList<>();
    private List<MarketElement> itemsForPurchase = new ArrayList<>();
    private FfParagraphData after;
    private FfParagraphData emptyHanded;
    private boolean giveUpMode;
    private int giveUpAmount;
    private boolean giveUpUnsuccessful;
    private int mustHaveGold;
    private int mustBuy;
    private String purchaseLabel;
    private String saleLabel;

    public String getSingleCcyKey() {
        return singleCcyKey;
    }

    public void setSingleCcyKey(final String singleCcyKey) {
        this.singleCcyKey = singleCcyKey;
    }

    public String getMultipleCcyKey() {
        return multipleCcyKey;
    }

    public void setMultipleCcyKey(final String multipleCcyKey) {
        this.multipleCcyKey = multipleCcyKey;
    }

    public List<MarketElement> getItemsForSale() {
        return itemsForSale;
    }

    public List<MarketElement> getItemsForPurchase() {
        return itemsForPurchase;
    }

    public FfParagraphData getAfter() {
        return after;
    }

    public void setAfter(final FfParagraphData after) {
        this.after = after;
    }

    @Override
    public CommandView getCommandView(final String rulesetPrefix) {
        final Map<String, Object> model = new HashMap<>();
        model.put("marketCommand", this);
        hideChoices(model);

        return new CommandView(rulesetPrefix + "Market", model);
    }

    @Override
    public String getValidMove() {
        return "finishMarketing";
    }

    @Override
    public MarketCommand clone() throws CloneNotSupportedException {
        final MarketCommand cloned = (MarketCommand) super.clone();
        cloned.after = cloneObject(after);
        cloned.emptyHanded = cloneObject(emptyHanded);
        cloned.itemsForSale = new ArrayList<>();
        for (final MarketElement item : itemsForSale) {
            cloned.itemsForSale.add(cloneObject(item));
        }
        cloned.itemsForPurchase = new ArrayList<>();
        for (final MarketElement item : itemsForPurchase) {
            cloned.itemsForPurchase.add(cloneObject(item));
        }
        return cloned;
    }

    public boolean isGiveUpMode() {
        return giveUpMode;
    }

    public void setGiveUpMode(final boolean giveUpMode) {
        this.giveUpMode = giveUpMode;
    }

    public int getGiveUpAmount() {
        return giveUpAmount;
    }

    public void setGiveUpAmount(final int giveUpAmount) {
        this.giveUpAmount = giveUpAmount;
    }

    public FfParagraphData getEmptyHanded() {
        return emptyHanded;
    }

    public void setEmptyHanded(final FfParagraphData emptyHanded) {
        this.emptyHanded = emptyHanded;
    }

    public boolean isGiveUpUnsuccessful() {
        return giveUpUnsuccessful;
    }

    public void setGiveUpUnsuccessful(final boolean giveUpUnsuccessful) {
        this.giveUpUnsuccessful = giveUpUnsuccessful;
    }

    public int getMustHaveGold() {
        return mustHaveGold;
    }

    public void setMustHaveGold(final int mustHaveGold) {
        this.mustHaveGold = mustHaveGold;
    }

    public String getPurchaseLabel() {
        return purchaseLabel;
    }

    public void setPurchaseLabel(final String purchaseLabel) {
        this.purchaseLabel = purchaseLabel;
    }

    public String getSaleLabel() {
        return saleLabel;
    }

    public void setSaleLabel(final String saleLabel) {
        this.saleLabel = saleLabel;
    }

    public int getMustBuy() {
        return mustBuy;
    }

    public void setMustBuy(final int mustBuy) {
        this.mustBuy = mustBuy;
    }

}
