package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;

public class NoChantStatistic extends AStatistic {

    private int _cpt;

    public NoChantStatistic() {
        super("Nombre de non chanteurs");
        _cpt = 0;
    }

    @Override
    public void computeStatistic() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            if (!locParticipant.isChante()) {
                _cpt++;
            }
        }
    }

    @Override
    public String printResult() {
        return String.format("%d participants sur %d ne chantent pas",
                _cpt, ParticipantManager.getInstance().getAllData().size());
    }
}
