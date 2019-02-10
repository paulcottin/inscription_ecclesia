package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;

public class NoGoodCodePostalStatistic extends AStatistic {

    private final int _maxVoeuxPerParticipant;

    public NoGoodCodePostalStatistic() {
        super("Code postaux français qui n'ont pas 5 chiffres et qui ne sont pas affiliés à une chorale");

        _maxVoeuxPerParticipant = EnumConfigProperty.NB_MAX_VOEUX.intV();
    }

    @Override
    public void computeStatistic() {

    }

    @Override
    public void printResult() {
        final StringBuilder locStringBuilder = new StringBuilder();
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            if (locParticipant.getRegion().getPays().isFrance() &&
                    locParticipant.getCodePostal().length() != _maxVoeuxPerParticipant &&
                    !locParticipant.isChoraleAffilie()) {
                locStringBuilder.append(String.format("%s, CP : %s\n",
                        locParticipant, locParticipant.getCodePostal())
                );
            }
        }
        if (locStringBuilder.length() == 0 ) {
            System.out.println("\tAucun problème");
        } else {
            System.out.println(locStringBuilder.toString());
        }
    }
}
