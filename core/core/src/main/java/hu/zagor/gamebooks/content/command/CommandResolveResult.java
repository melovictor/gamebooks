package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.content.ParagraphData;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for returning command result.
 * @author Tamas_Szekeres
 */
public class CommandResolveResult {

    private List<ParagraphData> resolveList = new ArrayList<>();
    private Boolean finished;

    public List<ParagraphData> getResolveList() {
        return resolveList;
    }

    public void setResolveList(final List<ParagraphData> resolveList) {
        this.resolveList = resolveList;
    }

    public boolean isFinished() {
        return finished == null ? (resolveList != null && !resolveList.isEmpty()) : finished;
    }

    public void setFinished(final boolean finished) {
        this.finished = finished;
    }

    /**
     * Adds a new {@link ParagraphData} object to the list of resolvable objects.
     * @param data the {@link ParagraphData} object to add
     */
    public void add(final ParagraphData data) {
        resolveList.add(data);
    }

    /**
     * Adds a list of {@link ParagraphData} objects to the list of resolvable objects.
     * @param dataList the list of {@link ParagraphData} objects to add
     */
    public void add(final List<ParagraphData> dataList) {
        resolveList.addAll(dataList);
    }
}
