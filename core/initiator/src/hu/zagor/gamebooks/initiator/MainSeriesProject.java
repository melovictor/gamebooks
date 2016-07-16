package hu.zagor.gamebooks.initiator;

import hu.zagor.gamebooks.initiator.collector.CollectorData;
import hu.zagor.gamebooks.initiator.collector.CreateCollectorProject;

public class MainSeriesProject {
    public static void main(final String[] args) {
        final CollectorData data = new CollectorData();
        data.setBookDirName("lw");
        data.setBookSeriesFullName("lonewolf");
        data.setBasicRuleset("lw");

        new CreateCollectorProject().create(data);
    }
}
