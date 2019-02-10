package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.groupe_evangelisation.SoloGeographique;
import org.ecclesiacantic.model.data_manager.SoloGeographiqueManager;
import org.ecclesiacantic.model.data_manager.bean.ChoraleManager;
import org.ecclesiacantic.model.statistic.AStatistic;

public class PaysNonGereStatistic extends AStatistic {

    public PaysNonGereStatistic() {
        super("Pays avec des solos geographiques dont le mapping n'est pas fait avec les groupe d'évangélisation");
    }

    @Override
    public void computeStatistic() {

    }

    @Override
    public void printResult() {
        final StringBuilder locStringBuilder = new StringBuilder();
        for (final SoloGeographique locSoloGeographique : SoloGeographiqueManager.getInstance().getAllData()) {
            if (!locSoloGeographique.getRegion().getPays().isFrance() &&
                    locSoloGeographique.getIdGroupeEvangelisation().equals(ChoraleManager.getInstance().getNullData().getIdGroupeEvangelisation())) {
                locStringBuilder.append(String.format("%s\n", locSoloGeographique.getRegion().getPays().toString()));
            }
        }
        if (locStringBuilder.length() == 0 ) {
            System.out.println("\tAucun problème");
        } else {
            System.out.println(locStringBuilder.toString());
        }
    }
}
