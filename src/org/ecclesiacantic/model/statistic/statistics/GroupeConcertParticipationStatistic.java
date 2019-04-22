package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.GroupeConcert;
import org.ecclesiacantic.model.data_manager.GroupeConcertManager;
import org.ecclesiacantic.model.statistic.AStatistic;

public class GroupeConcertParticipationStatistic extends AStatistic {

    public GroupeConcertParticipationStatistic() {
        super("Répartition du nombre de participant par groupe de concert");
    }

    @Override
    public void computeStatistic() {

    }

    @Override
    public String printResult() {
        int locTotalConcert = 0;
        final StringBuilder locStringBuilder = new StringBuilder(128);
        for (final GroupeConcert locGroupe : GroupeConcertManager.getInstance().getAllData()) {
            final int locGroupeNumber = locGroupe.getListParticipants().size();
            locStringBuilder.append(String.format("Groupe de concert N°%s : %d participants ; %s\n",
                    locGroupe.getName(), locGroupeNumber, locGroupe.toString())
            );
            locTotalConcert += locGroupeNumber;
        }
        locStringBuilder.append(String.format("Total de participants au concert : %d", locTotalConcert));
        return locStringBuilder.toString();
    }
}
