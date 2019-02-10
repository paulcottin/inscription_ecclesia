package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.HashMap;
import java.util.Map;

public class CheckNoSameMcParticipantStatistic extends AStatistic {

    private final Map<Participant, Map<MasterClass, Integer>> _cptMc;

    public CheckNoSameMcParticipantStatistic() {
        super("Vérification qu'on a pas attribué plusieurs fois la même MC à un participant");
        _cptMc = new HashMap<>();
    }

    @Override
    public void computeStatistic() {
        for (final Participant locPart : ParticipantManager.getInstance().getAllData()) {
            final Map<MasterClass, Integer> locAllMap = new HashMap<>();
            for (final Evenement locEvt : locPart.getEvenements()) {
                if (locAllMap.containsKey(locEvt.getMasterclass())) {
                    int locMcCpt = locAllMap.get(locEvt.getMasterclass());
                    locAllMap.put(locEvt.getMasterclass(), ++locMcCpt);
                } else {
                    locAllMap.put(locEvt.getMasterclass(), 1);
                }
            }

            final HashMap<MasterClass, Integer> locErrorMap = new HashMap<>();
            for (final Map.Entry<MasterClass, Integer> locEntry : locAllMap.entrySet()) {
                if (locEntry.getValue() > 1) {
                    locErrorMap.put(locEntry.getKey(), locEntry.getValue());
                }
            }

            if (locErrorMap.size() > 0) {
                _cptMc.put(locPart, locErrorMap);
            }
        }
    }

    @Override
    public void printResult() {
        if (_cptMc.size() == 0) {
            System.out.println("\tAucun participant ne fera deux fois la même MasterClasse");
        } else {
            final StringBuilder locStringBuilder = new StringBuilder(128);
            for (final Map.Entry<Participant, Map<MasterClass, Integer>> locEntry : _cptMc.entrySet()) {
                locStringBuilder.append(String.format("\tLe participant %s est inscrit : \n",
                        locEntry.getKey()));
                for (final Map.Entry<MasterClass, Integer> locInscription : locEntry.getValue().entrySet()) {
                    locStringBuilder.append(String.format("\t\t%d fois à la MC %s\n",
                            locInscription.getValue(), locInscription.getKey()));
                }
            }
            System.out.println(locStringBuilder.toString());
        }
    }
}
