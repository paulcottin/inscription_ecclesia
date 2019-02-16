package org.ecclesiacantic.model.data.beans;

import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.utils.CompareUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExport;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExportList;

import java.util.*;

public class Evenement implements Comparable<Evenement>{

    private final EnumCreneau _creneau;
    private List<Salle> _salleList;
    private final MasterClass _masterclass;
    private final List<Participant> _participants;
    private double _fullFactor;
    private final Map<Salle, List<Participant>> _participantRepartition;

    public Evenement(final EnumCreneau parCreneau, final MasterClass parMasterclass) {
        this._creneau = parCreneau;
        this._salleList = new ArrayList<>(1);
        this._masterclass = parMasterclass;
        this._participants = new ArrayList<>();
        this._participantRepartition = new HashMap<>();
        this._fullFactor = 1;
    }

    public final EnumCreneau getCreneau() {
        return _creneau;
    }

    public final boolean isSalleDispo(final EnumCreneau parCreneau) {
        if (getSalleList().isEmpty()) {
            return false;
        }
        for (final Salle locSalle : getSalleList()) {
            if (!locSalle.isDispo(parCreneau)) {
                return false;
            }
        }
        return true;
    }

    public final List<Salle> getSalleList() {
        return _salleList;
    }

    public final String getSallesString() {
        return getSalleList().toString();
    }

    public final void addSalles(final Collection<Salle> parSalles) {
        for (final Salle locSalle : parSalles) {
            addSalle(locSalle);
        }
    }

    public final void addSalle(final Salle parSalle) {
        if (!_salleList.contains(parSalle)) {
            if (_salleList.size() == _masterclass.getDiviserEn()) {
                System.err.println(String.format(
                        "L'événement %s ne peut pas avoir plus de %d salles",
                        this.toString(), _salleList.size()
                ));
            } else {
                this._salleList.add(parSalle);
                computeRepartitionPartInSalles();
            }
        }
    }

    /**
     * Pour toutes les salles prévues pour l'événement, on commencer par remplir de participant les plus
     * grandes salles.
     */
    private final void computeRepartitionPartInSalles() {
        final List<Salle> locMaxToMinSalleCapacity = (List<Salle>) CompareUtils.inverseSort(_salleList);

        //On met tous les participants dedans jusqu'à ce qu'il n'y ait plus de place
        final Iterator<Participant> locPartIterator = _participants.iterator();
        for (final Salle locSalle : locMaxToMinSalleCapacity) {
            for (int locI = 0; locI < locSalle.getCapacite(); locI++) {
                if (locPartIterator.hasNext()) {
                    if (_participantRepartition.containsKey(locSalle)) {
                        _participantRepartition.get(locSalle).add(locPartIterator.next());
                    } else {
                        final List<Participant> locList = new ArrayList<>();
                        locList.add(locPartIterator.next());
                        _participantRepartition.put(locSalle, locList);
                    }
                }
            }
        }
    }

    public final boolean isFull() {
        return getParticipants().size() >= getTotalCapacite() * _fullFactor;
    }

    public final MasterClass getMasterclass() {
        return _masterclass;
    }

    public final List<Participant> getParticipants() {
        return _participants;
    }

    public final List<Participant> getParticipants(final Salle parSalle) {
        if (_participantRepartition.size() == 0) {
            computeRepartitionPartInSalles();
        }
        if (_participantRepartition.containsKey(parSalle)) {
            return _participantRepartition.get(parSalle);
        } else if (_participants.size() == 0) {
            return new ArrayList<>();
        } else {
            System.err.println(String.format(
                    "La salle %s n'est pas prévue pour l'événement %s",
                    parSalle,
                    this.toString()
            ));
            return null;
        }
    }

    public final Salle getSalleFor(final Participant parParticipant) {
        if (_participantRepartition.size() == 0) {
            computeRepartitionPartInSalles();
        }
        for (final Map.Entry<Salle, List<Participant>> locEntry : _participantRepartition.entrySet()){
            if (locEntry.getValue().contains(parParticipant)) {
                return locEntry.getKey();
            }
        }
        System.err.println(String.format("Le participant %s n'a pas été affecté à une salle pour l'evt %s",
                parParticipant, this)
        );
        return null;
    }

    public final void clearParticipantRepartitionInSalles() {
        _participantRepartition.clear();
    }

    public final int getParticipantsNumber() {
        return _participants.size();
    }

    /**
     * @return La salle la moins occupée de l'événement
     */
    private final Salle getLessPlaceSalle() {
        if (_salleList.size() == 0) {
            System.err.println(String.format(
                    "Aucune salle n'est asociée à l'événement %s",
                    this.toString()
            ));
            return null;
        } else {
            return (Salle) CompareUtils.sort(_salleList).get(0);
        }
    }

    public final void addParticipant(final Participant parParticipant) {
        if (!_participants.contains(parParticipant)) {
            if (parParticipant.addEvenementIfCompatible(this)) {
                _participants.add(parParticipant);
                EvenementManager.getInstance().addCreneauUsage(_creneau, _masterclass);
            }
        }
    }

    public final void removeParticipant(final Participant parParticipant) {
        if (_participants.contains(parParticipant)) {
            _participants.remove(parParticipant);
            parParticipant.removeEvenement(this);
            EvenementManager.getInstance().removeCreneauUsage(_creneau, _masterclass);
        } else {
            System.err.println(String.format("Impossible de supprimer %s de l'événement %s, il n'y était pas.",
                    parParticipant.toString(), this.toString()));
        }
    }

    /**
     * Permet de récupérer le participant dont la priorité du voeu pour cette
     * masterclass était la moindre
     * @return null si il n'y a aucun participant inscrit dans cette MC à ce créneau
     */
    public final Participant getLessPriorityParticipant() {
        if (_participants.size() == 0) {
            return null;
        }

        final List<Participant> locCheckedList = getLessPriorityParticipantHelper(true);
        if (locCheckedList.size() > 0) {
            return locCheckedList.get(0);
        } else {
            return getLessPriorityParticipantHelper(false).get(0);
        }
    }

    /**
     * retourne une liste ordonnée de participant en fonction de la priorité qu'ils avaient donné
     * à la masterclass courante.
     * Cette liste permet de savoir quel participant sortir de l'événement
     * @param parIsSkipAllScheduledEvtPart : dis si il faut prendre dans la liste un participant qui
     *                                     a déjà tout ses choix en place
     * @return
     */
    private final List<Participant> getLessPriorityParticipantHelper(final boolean parIsSkipAllScheduledEvtPart) {
        final List<Participant> locCheckedList = new ArrayList<>();

        final int locMinPriorityResetValue = 1000;
        int locMinPriority = locMinPriorityResetValue;
        Participant locTempMinPart = null;

        while (locCheckedList.size() != _participants.size()) {
            for (final Participant locPartcipant : _participants) {
                if (parIsSkipAllScheduledEvtPart && locPartcipant.isAllEvtScheduled()) {
                    continue;
                }
                final int locTmpPriority = locPartcipant.getPriorityOf(_masterclass);
                if (locTmpPriority < locMinPriority && !locCheckedList.contains(locPartcipant)) {
                    locMinPriority = locTmpPriority;
                    locTempMinPart = locPartcipant;
                }
            }
            locCheckedList.add(locTempMinPart);
            locMinPriority = locMinPriorityResetValue;
        }
        return locCheckedList;
    }

    public final int getTotalCapacite() {
        int locTotal = 0;
        for (final Salle locSalle : getSalleList()) {
            locTotal += locSalle.getCapacite();
        }
        return locTotal;
    }

    public final double getFullFactor() {
        return _fullFactor;
    }

    public final void setFullFactor(double _fullFactor) {
        this._fullFactor = _fullFactor;
    }

    /**
     * Permet de vérifier si la salle est en surpopulation et de combien.
     * @return si > 0 le nb de personnes en trop, si < 0 alors pop OK
     */
    public final int getSalleSurPop() {
        return _participants.size() - getTotalCapacite();
    }

    public final List<String> exportDataToCSV() {
        final List<String> locExportList = new ArrayList<>();
        locExportList.add(String.format("%s", _creneau));
        locExportList.add(String.format("%s", _salleList));
        locExportList.add(String.valueOf(getTotalCapacite()));
        locExportList.add(String.format("%s", _masterclass));
        locExportList.add(String.valueOf(_participants != null ? _participants.size() : -1));
        return locExportList;
    }

    public final Map<Salle, List<List<String>>> exportMasterclassData() {
        final Map<Salle, List<List<String>>> locData = new HashMap<>();
        for (final Salle locSalle : getSalleList()) {
            final List<List<String>> locDataList = new ArrayList<>();
            for (final Participant locPart : getParticipants(locSalle)) {
                final List<String> locPartInfos = new ArrayList<>();
                locPartInfos.add(locPart.getPrenom());
                locPartInfos.add(locPart.getNom());
                locDataList.add(locPartInfos);
            }
            locData.put(locSalle, locDataList);
        }
        return locData;
    }

    public final List<String> exportSallePopulation() {
        final List<String> locData = new ArrayList<>();
        locData.add(_masterclass.getName());
        locData.add(_salleList.toString());
        locData.add(String.valueOf(getTotalCapacite()));
        locData.add(String.valueOf(getParticipantsNumber()));
        return locData;
    }

    /**
     * Permet de comparer les evenement en terme de nombre places minimales à avoir dans une salle
     * @param parOther
     * @return > 1 si this.place_mini > other.place_mini
     */
    @Override
    public final int compareTo(final Evenement parOther) {
        return Integer.compare(
                _masterclass.getSessionMediumSize(),
                parOther.getMasterclass().getMinSalleCapacity()
        );
    }

    @Override
    public final String toString() {
        return String.join("\n", exportDataToCSV()).replace("\n", " ");
    }
}
