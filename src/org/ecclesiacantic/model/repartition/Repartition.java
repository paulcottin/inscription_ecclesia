package org.ecclesiacantic.model.repartition;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.model.data_manager.bean.MasterClassManager;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.EnumStatistics;
import org.ecclesiacantic.model.statistic.StatisticManager;
import org.ecclesiacantic.model.statistic.statistics.CheckVoeuxDifferentsParParticipantStatistic;
import org.ecclesiacantic.model.statistic.statistics.OccupationCreneauStatistic;
import org.ecclesiacantic.utils.CompareUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Repartition {

    final EvenementManager _evtMng;

    private double _fullFactor;
    private int _lessUsedMcNumber;
    private OccupationCreneauStatistic _occupationCreneauStat = (OccupationCreneauStatistic) StatisticManager.getInstance().getStatistics().get(EnumStatistics.OCCUPATION_CRENEAUX);
    private RepartitionResult _result;

    public Repartition() {
        this(-1.0, -1);
    }

    public Repartition(final double parFullFactor, final int parLessUsedMcNumber) {
        _evtMng = EvenementManager.getInstance();
        _fullFactor = parFullFactor >= 0.0 ? parFullFactor : 1.0;
        _lessUsedMcNumber = parLessUsedMcNumber >= 0 ? parLessUsedMcNumber : EnumConfigProperty.LESS_POP_MC_NB.intV();
    }

    /**
     * Reset de toutes les données calculées par une potentielle précédente répartition
     * afin de ne pas fausser la répartition courante
     */
    private final void reset() {
        ParticipantManager.getInstance().reset();
        EvenementManager.getInstance().reset();
        MasterClassManager.getInstance().reset();

        MasterClassManager.getInstance().setLessPopularMcLimit(_lessUsedMcNumber);

        _occupationCreneauStat.resetStatistic();
    }

    /**
     * Effectue la répartition des MC en fonction des salles et des participants
     */
    public final void run() {
        reset();
        //On initialise les besoins de salles des MC en fonction de la demande
        MasterClassManager.getInstance().computeMasterClassScore();

        // On ajoute les salles imposées
        for (final Evenement locEvt : EvenementManager.getInstance().getAllEvents()) {
            final List<Salle> locImposedSalleList = locEvt.getMasterclass().getImposedSalleList(locEvt.getCreneau());
            if (locImposedSalleList != null) {
                locEvt.addSalles(locImposedSalleList);
            }
        }

        // On essaie de remplir _fullFactor des salles avec les MCs posant problèmes :
        //      les moins voulues
        //      Celles qui n'ont pas tous leurs créneaux disponibles
        EvenementManager.getInstance().setFullFactor(_fullFactor);
        dispatchLessWantedMasterClasses();
        dispatchNotAllCreneauxMc();

        EvenementManager.getInstance().setFullFactor(1.0);

        // Est-ce qu'on utilise la liste des participants triée ou non ?
        final Collection<Participant> locParticipants = EnumConfigProperty.IS_USE_SORTED_PARTICIPANTS.boolV() ?
                (Collection<Participant>) CompareUtils.inverseSort(ParticipantManager.getInstance().getAllData()) :
                ParticipantManager.getInstance().getAllData();

        final Set<Participant> locMalformedListVoeux;
        if (EnumConfigProperty.IS_SKIP_MALFORMED_VOEUX.boolV()) {
            locMalformedListVoeux = ((CheckVoeuxDifferentsParParticipantStatistic) StatisticManager.getInstance().getStatistics().get(EnumStatistics.SAME_VOEUX_FOR_PARTICIPANT)).getCptVoeux().keySet();
        } else {
            locMalformedListVoeux = new HashSet<>();
        }

        for (final Participant locParticipant : locParticipants) {
            //Si le participant a mal rempli ses voeux et que le mode est désactivé il ne passe pas
            if (locMalformedListVoeux.contains(locParticipant)) {
                continue;
            }
            computeRunForAParticipant(locParticipant);
        }

        System.out.println("Avant rééquilibrage");
        _occupationCreneauStat.printResult();

        _occupationCreneauStat.resetStatistic();
        _occupationCreneauStat.computeStatistic();
        _occupationCreneauStat.printResult();

        _evtMng.exportSallePopulation(false);
    }

    /**
     * Permet de donner à un participant une MC pas populaire si il l'a dans ses voeux.
     * Se base sur une constante définie dans la configuration
     */
    private final void dispatchLessWantedMasterClasses() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            // Si le participant a un de ses choix dans la liste des MCs les moins populaire, on lui donne celle-ci
            final List<MasterClass> locLessPopMcList = MasterClassManager.getInstance().getLessPopularMc(locParticipant);
            if (!locLessPopMcList.isEmpty()) {
                for (final MasterClass locMc : locLessPopMcList) {
                    final Evenement locEvt = _evtMng.getAvailableEvtOrNextPartChoice(locParticipant, locMc, locParticipant.getPriorityOf(locMc), true);
                    if (locEvt != null) {
                        locEvt.addParticipant(locParticipant);
                    }
                }
            }
        }
    }

    /**
     * Permet de donner à un participant une MC dont la salle est très vaste si il l'a dans ses voeux.
     * Se base sur une constante définie dans la configuration
     */
    private final void dispatchHugeSalles() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            // Si le participant a un de ses choix dans la liste des MCs dont les salles sont les plus grandes, on lui donne celle-ci
            final List<MasterClass> locHugeSallesMc = MasterClassManager.getInstance().getHugeSalleForMc(locParticipant);
            if (!locHugeSallesMc.isEmpty()) {
                for (final MasterClass locMc : locHugeSallesMc) {
                    final Evenement locEvt = _evtMng.getAvailableEvtOrNextPartChoice(locParticipant, locMc, locParticipant.getPriorityOf(locMc), true);
                    if (locEvt != null) {
                        locEvt.addParticipant(locParticipant);
                    }
                }
            }
        }
    }

    /**
     * Permet de donner à un participant une MC qui n'est pas dispo à tous les créneaux (MC + salle) si il l'a dans ses voeux.
     */
    private final void dispatchNotAllCreneauxMc() {
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            // Si le participant a un de ses choix dans la liste des MCs dont les salles sont les plus grandes, on lui donne celle-ci
            final List<MasterClass> locNotAllCreneauMcSet = MasterClassManager.getInstance().getNotAllCreneauxDispoFor(locParticipant);
            if (!locNotAllCreneauMcSet.isEmpty()) {
                for (final MasterClass locMc : locNotAllCreneauMcSet) {
                    final Evenement locEvt = _evtMng.getAvailableEvtOrNextPartChoice(locParticipant, locMc, locParticipant.getPriorityOf(locMc), true);
                    if (locEvt != null) {
                        locEvt.addParticipant(locParticipant);
                    }
                }
            }
        }
    }

    private final void computeRunForAParticipant(final Participant parParticipant) {
        for (int locPriority = 0; locPriority < parParticipant.getVoeux().size(); locPriority++) {
            final Evenement locEvt = EvenementManager.getInstance().getAvailableEvtOrNextPartChoice(
                    parParticipant,
                    parParticipant.getVoeu(locPriority),
                    locPriority
            );
            if (locEvt == null) {
                continue;
            }
            if (!parParticipant.isEvenementCompatible(locEvt)) {
                continue;
            }

            if (locEvt.isFull()) {
                continue;
            }

            if (!locEvt.isSalleDispo(locEvt.getCreneau())) {
                continue;
            }
            locEvt.addParticipant(parParticipant);
        }
    }

    public final double getFullFactor() {
        return _fullFactor;
    }

    public final int getLessUsedMcNumber() {
        return _lessUsedMcNumber;
    }

    public final RepartitionResult getResult() {
        return _result;
    }

    public final void setResult(final RepartitionResult _result) {
        this._result = _result;
    }
}
