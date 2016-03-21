package hu.zagor.gamebooks.initiator;

import hu.zagor.gamebooks.initiator.collector.CollectorData;
import hu.zagor.gamebooks.initiator.collector.CreateCollectorProject;

public class MainSeriesProject {
    public static void main(final String[] args) {
        final CollectorData data = new CollectorData();
        data.setBookDirName("eq");
        data.setBookSeriesFullName("endlessquest");
        data.setBasicRuleset("raw");

        new CreateCollectorProject().create(data);
    }
}
