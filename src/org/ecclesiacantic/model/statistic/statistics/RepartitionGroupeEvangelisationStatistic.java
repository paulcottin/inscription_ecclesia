package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data_manager.GroupeEvangelisationManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.HashMap;
import java.util.Map;

public class RepartitionGroupeEvangelisationStatistic extends AStatistic{

    private final Map<GroupeEvangelisation, Integer> _cpts;

    public RepartitionGroupeEvangelisationStatistic() {
        super("Remplissage des groupes d'évangélisation");
        _cpts = new HashMap<>();
    }

    @Override
    public void computeStatistic() {

    }

    @Override
    public void printResult() {
        final StringBuilder locStringBuilder = new StringBuilder(128);
        for (final GroupeEvangelisation locGroupe : GroupeEvangelisationManager.getInstance().getAllData().values()) {
            locStringBuilder.append(String.format("Groupe %s '%s' : %d participants (%d solos géo) - Régions : %s\n",
                    locGroupe.getId(), locGroupe.getChoraleReferente().getName(),
                    locGroupe.getListParticipants().size(), locGroupe.getPartNonAffilies().size(),
                    locGroupe.getRegions().toString()
                    )
            );
        }
        System.out.println(locStringBuilder.toString());
    }
}
