package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NbSalleByMasterclassStatistic extends AStatistic {

    private final Map<MasterClass, List<String>> _sallesMc;

    public NbSalleByMasterclassStatistic() {
        super("Vérification du nombre de salle données par Masterclasse");
        _sallesMc = new HashMap<>();
    }

    @Override
    public void computeStatistic() {
        for (final Evenement locEvt : EvenementManager.getInstance().getAllEvents()) {
            if (_sallesMc.containsKey(locEvt.getMasterclass())) {
                if (!_sallesMc.get(locEvt.getMasterclass()).contains(locEvt.getSallesString())) {
                    _sallesMc.get(locEvt.getMasterclass()).add(locEvt.getSallesString());
                }
            } else {
                final List<String> locSallesList = new ArrayList<>(1);
                locSallesList.add(locEvt.getSallesString());
                _sallesMc.put(locEvt.getMasterclass(), locSallesList);
            }
        }
    }

    @Override
    public String printResult() {
        int locSalleOk = 0, locSalleKo = 0;
        final StringBuilder locResultBuilder = new StringBuilder(256);
        for (final Map.Entry<MasterClass, List<String>> locEntry : _sallesMc.entrySet()) {
            if (locEntry.getValue().size() < 2) {
                locResultBuilder.append(String.format("MC %s : OK => %d salle\n",
                        locEntry.getKey(),
                        locEntry.getValue().size())
                );
                locSalleOk++;
            } else {
                locResultBuilder.append(String.format("MC %s : KO => %s salles (%s)\n",
                        locEntry.getKey(),
                        locEntry.getValue().size(),
                        locEntry.getValue().toString())
                );
                locSalleKo++;
            }
        }
        // #TODO : Supprimer le code précédent
//        System.out.println(locResultBuilder.toString());
        return String.format("MCs OK : %d\nMCs KO : %d", locSalleOk, locSalleKo);
    }
}
