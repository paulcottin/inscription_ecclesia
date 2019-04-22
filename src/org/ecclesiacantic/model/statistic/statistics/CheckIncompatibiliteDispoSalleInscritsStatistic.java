package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.model.statistic.AStatistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckIncompatibiliteDispoSalleInscritsStatistic extends AStatistic {

    final Map<Salle, List<EnumCreneau>> _cptDispoSalle;
    final Map<MasterClass, List<EnumCreneau>> _cptDispoMc;
//    final Map<Evenement, List<EnumCreneau>> _cptDispoSalle, _cptDispoMc;

    public CheckIncompatibiliteDispoSalleInscritsStatistic() {
        super("Vérification qu'on a pas assigné des salles ou des MasterClasses lorsqu'ils n'étaient pas disponibles");
        _cptDispoMc = new HashMap<>();
        _cptDispoSalle = new HashMap<>();
    }

    @Override
    public void computeStatistic() {
        for (final Evenement locEvt : EvenementManager.getInstance().getAllEvents()) {
            //Vérification qu'une salle n'a pas été affectée alors qu'elle n'était pas dispo
            if (!locEvt.isSalleDispo(locEvt.getCreneau())) {
                if (locEvt.getSalleList().isEmpty()) {
                    continue;
                }
                if (_cptDispoSalle.containsKey(locEvt.getSalleList().get(0))) {
                    _cptDispoSalle.get(locEvt.getSalleList().get(0)).add(locEvt.getCreneau());
                } else {
                    final List<EnumCreneau> locCreneaux = new ArrayList<>(1);
                    locCreneaux.add(locEvt.getCreneau());
                    _cptDispoSalle.put(locEvt.getSalleList().get(0), locCreneaux);
                }
            }

            // Vérification qu'une MC n'ait pas été affectée alors qu'elle n'était pas dispo
            if (!locEvt.getMasterclass().isDispo(locEvt.getCreneau())) {
                if (_cptDispoMc.containsKey(locEvt.getMasterclass())) {
                    _cptDispoMc.get(locEvt.getMasterclass()).add(locEvt.getCreneau());
                } else {
                    final List<EnumCreneau> locCreneaux = new ArrayList<>(1);
                    locCreneaux.add(locEvt.getCreneau());
                    _cptDispoMc.put(locEvt.getMasterclass(), locCreneaux);
                }
            }
        }
    }

    @Override
    public String printResult() {
        final StringBuilder locAllBuilder = new StringBuilder(256);
        if (_cptDispoMc.size() == 0) {
            locAllBuilder.append("\tAucune affectation de MC alors qu'elle n'était pas dispo");
        } else {
            final StringBuilder locMcBuilder = new StringBuilder(128);
            for (final Map.Entry<MasterClass, List<EnumCreneau>> locEntry : _cptDispoMc.entrySet()) {
                locMcBuilder.append(String.format(
                        "\tLa MC %s a été prévue aux créneaux %s alors qu'elle n'est pas disponible\n",
                        locEntry.getKey(), locEntry.getValue().toString())
                );
            }
            locMcBuilder.append("\n");
            locAllBuilder.append(locMcBuilder.toString());
        }

        if (_cptDispoSalle.size() == 0) {
            locAllBuilder.append("\tAucune affectation de salle alors qu'elle n'était pas dispo");
        } else {
            final StringBuilder locSalleBuilder = new StringBuilder(128);
            for (final Map.Entry<Salle, List<EnumCreneau>> locEntry : _cptDispoSalle.entrySet()) {
                locSalleBuilder.append(String.format(
                        "\tLa salle %s a été prévue au(x) créneau(x) %s alors qu'elle n'est pas disponible\n",
                        locEntry.getKey().toString(), locEntry.getValue().toString())
                );
            }
            locAllBuilder.append(locSalleBuilder.toString());
        }
        return locAllBuilder.toString();
    }
}
