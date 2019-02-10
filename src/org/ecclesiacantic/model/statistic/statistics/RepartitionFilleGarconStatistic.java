package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;
import org.ecclesiacantic.utils.parser.NumberUtils;

public class RepartitionFilleGarconStatistic extends AStatistic {

    private int _filleCpt, _garconCpt;
    private double _fillePct, _garconPct;

    public RepartitionFilleGarconStatistic() {
        super("Répartition des filles et garçons");
        _filleCpt = 0;
        _garconCpt = 0;
    }

    @Override
    public void computeStatistic() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            if (locParticipant.getCivilite().isFille()) {
                _filleCpt++;
            } else {
                _garconCpt++;
            }
        }
        final int locTotal = _filleCpt + _garconCpt;
        _fillePct = NumberUtils.computePourcentage(_filleCpt, locTotal);
        _garconPct = NumberUtils.computePourcentage(_garconCpt, locTotal);

    }

    @Override
    public void printResult() {
        final StringBuilder locResultBuilder = new StringBuilder(128);
        locResultBuilder.append(String.format("Nombre de filles : %d (%s %%)\n", _filleCpt, _fillePct))
                .append(String.format("Nombre de garçons : %d(%s %%)\n", _garconCpt, _garconPct));
        System.out.println(locResultBuilder.toString());
    }
}
