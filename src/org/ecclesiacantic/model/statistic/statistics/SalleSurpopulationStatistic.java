package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.HashMap;
import java.util.Map;

public class SalleSurpopulationStatistic extends AStatistic{

    private final Map<Evenement, Integer> _surpop, _lowpop;
    private int _surpopCpt, _lowpopCpt, _totalSurpop, _totalLowpop;

    public SalleSurpopulationStatistic() {
        super("Vérification de la surpopulation si des salles sont en surpopulation");
        _surpop = new HashMap<>();
        _lowpop = new HashMap<>();
        _surpopCpt = 0;
        _lowpopCpt = 0;
        _totalSurpop = 0;
        _totalLowpop = 0;
    }

    @Override
    public void computeStatistic() {
        for (final Evenement locEvt : EvenementManager.getInstance().getAllEvents()) {
            final int locSurpop = locEvt.getSalleSurPop();
            if (locSurpop > 0) {
                _surpop.put(locEvt, locSurpop);
                _totalSurpop++;
                _surpopCpt += locSurpop;
            } else {
                _lowpop.put(locEvt, -locSurpop);
                _totalLowpop++;
                _lowpopCpt += -locSurpop;
            }
        }
    }

    @Override
    public String printResult() {
//        if (_surpop.size() == 0) {
//            System.out.println("\tAucun problème de surpopulation de salle");
//        } else {
            final StringBuilder locStringBuilder = new StringBuilder(128);
            for (final Map.Entry<Evenement, Integer> locEntry : _surpop.entrySet()) {
                locStringBuilder.append(String.format("\tLa salle %s (capacité %d) est en surpopulation de %d personnes / %d (%s) MC : %s\n",
                        locEntry.getKey().getSallesString(), locEntry.getKey().getTotalCapacite(),
                        locEntry.getValue(), locEntry.getKey().getParticipants().size(),
                        locEntry.getKey().getCreneau(), locEntry.getKey().getMasterclass()));
            }
            for (final Map.Entry<Evenement, Integer> locEntry : _lowpop.entrySet()) {
                locStringBuilder.append(String.format("\tLa salle %s (capacité %d) est sous utilisée de %d personnes / %d (%s) MC : %s\n",
                        locEntry.getKey().getSallesString(), locEntry.getKey().getTotalCapacite(),
                        locEntry.getValue(), locEntry.getKey().getParticipants().size(),
                        locEntry.getKey().getCreneau(), locEntry.getKey().getMasterclass()));
            }
            locStringBuilder.setLength(0);
            locStringBuilder.append(String.format("\nNombre de salle en surpopulation : %d\n", _surpop.size()));
            locStringBuilder.append(String.format("\nNombre de salle en surpopulation : %d\n", _surpop.size()));
            locStringBuilder.append(String.format("Nombre d'evt en surpop : %d, en sous pop %d\n",
                    _totalSurpop, _totalLowpop));
            locStringBuilder.append(String.format("Total de surpopulation : %d, de sous population %d\n",
                    _surpopCpt, _lowpopCpt));
            return locStringBuilder.toString();
//        }
    }
}
