package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data_manager.GroupeEvangelisationManager;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.ArrayList;
import java.util.List;

public class NotFoundParticipantStatistic extends AStatistic {

    private final List<Participant> _notFound;

    public NotFoundParticipantStatistic() {
        super("Liste des participants qui ne sont pas dans des groupes d'évangélisation");
        _notFound = new ArrayList<>();
    }

    @Override
    public void computeStatistic() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            boolean locIsFound = false;
            for (final GroupeEvangelisation locGroupe : GroupeEvangelisationManager.getInstance().getAllData().values()) {
                if (locGroupe.getListParticipants().contains(locParticipant)) {
                    locIsFound = true;
                    break;
                }
            }
            if (!locIsFound && locParticipant.isChante()) {
                _notFound.add(locParticipant);
            }
        }
    }

    @Override
    public void printResult() {
        final StringBuilder locStringBuilder = new StringBuilder();
        for (final Participant locParticipant : _notFound) {
            locStringBuilder.append(String.format("%s, CP %s\n",
                    locParticipant.toString(),
                    locParticipant.getCodePostal()));
        }
        if (locStringBuilder.length() == 0 ) {
            System.out.println("\tTous les participants voulant chanter sont dans des groupes d'évangélisation");
        } else {
            System.out.println(locStringBuilder.toString());
        }
    }
}
