package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;
import org.ecclesiacantic.utils.parser.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SatisfactionVoeuxStatisic extends AStatistic {

    private final Map<Integer, Integer> _voeuSatisfaction;
    private final Map<Integer, Double> _voeuSatisfactionPct;

    public SatisfactionVoeuxStatisic() {
        super("Satisfaction des voeux par rapport aux demandes");
        _voeuSatisfaction = new HashMap<>();
        _voeuSatisfactionPct = new HashMap<Integer, Double>();
    }

    @Override
    public void computeStatistic() {
        final int locTotalParticipantNumber = ParticipantManager.getInstance().getAllData().size();
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            for (int locIndex = 0; locIndex < locParticipant.getVoeux().size(); locIndex++) {
                final List<MasterClass> locPartMc = getMasterclassesFromEvts(locParticipant.getEvenements());
                if (locPartMc.contains(locParticipant.getVoeu(locIndex))) {
                    if (_voeuSatisfaction.containsKey(locIndex)) {
                        int locCurrent = _voeuSatisfaction.get(locIndex);
                        _voeuSatisfaction.put(locIndex, ++locCurrent);
                    } else {
                        _voeuSatisfaction.put(locIndex, 1);
                    }
                }
            }
        }
        for (final Map.Entry<Integer, Integer> locVoeuSati : _voeuSatisfaction.entrySet()) {
            _voeuSatisfactionPct.put(
                    locVoeuSati.getKey(),
                    NumberUtils.computePourcentage(locVoeuSati.getValue(), locTotalParticipantNumber)
            );
        }
    }

    private final List<MasterClass> getMasterclassesFromEvts(final List<Evenement> parEvts) {
        final List<MasterClass> locReturnList = new ArrayList<>(parEvts.size());
        for (final Evenement locEvt : parEvts) {
            locReturnList.add(locEvt.getMasterclass());
        }
        return locReturnList;
    }

    @Override
    public String printResult() {
        final StringBuilder locResultBuilder = new StringBuilder(128);
        for (final Map.Entry<Integer, Integer> locVoeuSati : _voeuSatisfaction.entrySet()) {
            locResultBuilder.append(String.format("Nombre de participants ayant eu leur choix n°%d : %d (%s %%)\n",
                    locVoeuSati.getKey() + 1, locVoeuSati.getValue(),
                    _voeuSatisfactionPct.get(locVoeuSati.getKey()))
            );
        }
        return locResultBuilder.toString();
    }
}
