package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.GroupeConcert;
import org.ecclesiacantic.model.data.beans.participant.EnumPupitre;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data_manager.GroupeConcertManager;
import org.ecclesiacantic.model.data_manager.GroupeEvangelisationManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.HashMap;
import java.util.Map;

public class NombrePersonneParVoixStatistic extends AStatistic {

    final Map<GroupeConcert, Map<EnumPupitre, Integer>> _groupeConcertMap;
    final Map<GroupeEvangelisation, Map<EnumPupitre, Integer>> _groupeEvangelisationMap;

    public NombrePersonneParVoixStatistic() {
        super("Nombre de personnes par voix pour les groupes d'évangélisation + concert");
        _groupeConcertMap = new HashMap<>();
        _groupeEvangelisationMap = new HashMap<>();
    }

    @Override
    public void computeStatistic() {
        // Groupe Evangelisation
        for (final GroupeEvangelisation locGroupe : GroupeEvangelisationManager.getInstance().getAllData().values()) {
            if (!_groupeEvangelisationMap.containsKey(locGroupe.getName())) {
                _groupeEvangelisationMap.put(locGroupe, initPupitreMap());
            }
            for (final Participant locPart : locGroupe.getListParticipants()) {
                int locPartPupitreCpt = _groupeEvangelisationMap.get(locGroupe).get(locPart.getPupitre());
                _groupeEvangelisationMap.get(locGroupe).put(locPart.getPupitre(), ++locPartPupitreCpt);
            }
        }

        // Groupe Evangelisation
        for (final GroupeConcert locGroupe : GroupeConcertManager.getInstance().getAllData()) {
            if (!_groupeConcertMap.containsKey(locGroupe.getName())) {
                _groupeConcertMap.put(locGroupe, initPupitreMap());
            }
            for (final Participant locPart : locGroupe.getListParticipants()) {
                int locPartPupitreCpt = _groupeConcertMap.get(locGroupe).get(locPart.getPupitre());
                _groupeConcertMap.get(locGroupe).put(locPart.getPupitre(), ++locPartPupitreCpt);
            }
        }
    }

    @Override
    public String printResult() {
        final StringBuilder locStringBuilder = new StringBuilder(128);
        locStringBuilder.append("\tGroupes d'évangélisation : \n");
        for (final Map.Entry<GroupeEvangelisation, Map<EnumPupitre, Integer>> locEntry : _groupeEvangelisationMap.entrySet()) {
            locStringBuilder.append(String.format(
                    "\t\t%s (%s) : Sopranes : %s, Alti : %s, Ténors : %s, Basses : %s, ne sais pas : %s\n",
                    locEntry.getKey().getId(),
                    locEntry.getKey().getChoraleReferente().getName(),
                    //TODO utiliser une boucle
                    locEntry.getValue().get(EnumPupitre.SOPRANO),
                    locEntry.getValue().get(EnumPupitre.ALTO),
                    locEntry.getValue().get(EnumPupitre.TENOR),
                    locEntry.getValue().get(EnumPupitre.BASSE),
                    locEntry.getValue().get(EnumPupitre.NE_SAIS_PAS)
            ));
        }

        locStringBuilder.append("\n\tGroupes de concert : \n");
        for (final Map.Entry<GroupeConcert, Map<EnumPupitre, Integer>> locEntry : _groupeConcertMap.entrySet()) {
            locStringBuilder.append(String.format(
                    "\t\t%s : Sopranes : %s, Alti : %s, Ténors : %s, Basses : %s, ne sais pas : %s\n",
                    locEntry.getKey(),
                    locEntry.getValue().get(EnumPupitre.SOPRANO),
                    locEntry.getValue().get(EnumPupitre.ALTO),
                    locEntry.getValue().get(EnumPupitre.TENOR),
                    locEntry.getValue().get(EnumPupitre.BASSE),
                    locEntry.getValue().get(EnumPupitre.NE_SAIS_PAS)
            ));
        }

        return locStringBuilder.toString();

    }

    private final Map<EnumPupitre, Integer> initPupitreMap() {
        final Map<EnumPupitre, Integer> locReturnMap = new HashMap<>();
        for (final EnumPupitre locPupitre : EnumPupitre.values()) {
            locReturnMap.put(locPupitre, 0);
        }
        return locReturnMap;
    }
}
