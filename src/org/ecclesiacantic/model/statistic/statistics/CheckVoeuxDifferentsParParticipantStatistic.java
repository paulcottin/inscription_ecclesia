package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.HashMap;
import java.util.Map;

public class CheckVoeuxDifferentsParParticipantStatistic extends AStatistic {

    private final Map<Participant, Map<MasterClass, Integer>> _cptVoeux;

    public CheckVoeuxDifferentsParParticipantStatistic() {
        super("Vérification si un participant n'a pas plusieurs fois le même voeu",
                true);
        _cptVoeux = new HashMap<>();
    }

    @Override
    public void computeStatistic() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            final HashMap<MasterClass, Integer> locParticipantVoeux = new HashMap<>();
            //On compte combien de fois le participant à choisi un voeu
            for (final MasterClass locVoeu : locParticipant.getVoeux()) {
                if (!locParticipantVoeux.containsKey(locVoeu)) {
                    locParticipantVoeux.put(locVoeu, 1);
                } else {
                    int locNbSameVoeu = locParticipantVoeux.get(locVoeu);
                    locParticipantVoeux.put(locVoeu, ++locNbSameVoeu);
                }
            }
            //Si une MC est choisie plus d'une fois on ajoute l'erreur dans le compteur global
            final Map<MasterClass, Integer> locErrorMap = new HashMap<>();
            for (final Map.Entry<MasterClass, Integer> locEntry : locParticipantVoeux.entrySet()) {
                if (locEntry.getValue() > 1) {
                    locErrorMap.put(locEntry.getKey(), locEntry.getValue());
                }
            }
            if (locErrorMap.size() > 0) {
                _cptVoeux.put(locParticipant, locErrorMap);
            }
        }
    }

    @Override
    public void printResult() {
        if (_cptVoeux.size() == 0) {
            System.out.println("Aucun participant n'a choisi plusieurs fois le même voeu");
        } else {
            final StringBuilder locStringBuilder = new StringBuilder(128);
            for (final Map.Entry<Participant, Map<MasterClass, Integer>> locEntry : _cptVoeux.entrySet()) {
                locStringBuilder.append(String.format("Le participant %s a : \n", locEntry.getKey()));
                for (final Map.Entry<MasterClass, Integer> locVoeux : locEntry.getValue().entrySet()) {
                    locStringBuilder.append(String.format("\t%d fois le voeu %s\n",
                            locVoeux.getValue(), locVoeux.getKey()));
                }
            }
            System.out.println(locStringBuilder.toString());
        }
    }

    public final Map<Participant, Map<MasterClass, Integer>> getCptVoeux() {
        return _cptVoeux;
    }
}
