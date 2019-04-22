package org.ecclesiacantic.model.data.groupe_evangelisation;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.itf.IExportableObject;
import org.ecclesiacantic.model.data.beans.participant.Chorale;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;

import java.util.ArrayList;
import java.util.List;

public class GroupeEvangelisation implements IExportableObject {

    private final String _id;
    private List<ARegion> _regions;
    private int _limite;
    private Chorale _choraleReferente;
    private final List<Participant> _partNonAffilies;

    public GroupeEvangelisation(final String parId, final Chorale parChoraleReferente) {
        this._id = parId;
        this._choraleReferente = parChoraleReferente;
        this._partNonAffilies = new ArrayList<>();
        this._regions = new ArrayList<>();
    }

    public final void addParticipantNonAffilie(final Participant participantNonAffilie) {
        if (_partNonAffilies.contains(participantNonAffilie)) {
            System.err.println(
                    String.format("Le participant %s est déjà présent dans le groupe d'évangélisation %s",
                            participantNonAffilie.toString(),
                            this.toString()));
        } else if (participantNonAffilie.isChoraleAffilie()) {
            System.err.println(
                    String.format("Le participant %s fait parti d'une chorale : %s",
                            participantNonAffilie.toString(),
                            participantNonAffilie.getChorale().toString())
            );
        } else if (!participantNonAffilie.isChante()) {
            System.err.println(
                    String.format("Le participant %s ne veut pas chanter, on ne l'inscrit pas",
                            participantNonAffilie.toString())
            );
        } else {
            _partNonAffilies.add(participantNonAffilie);
        }
    }

    public final List<Participant> getListParticipants() {
        final List<Participant> locReturnList = new ArrayList<>();
        if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
            locReturnList.addAll(_choraleReferente.getParticipants());
            for (final Chorale locChorale : _choraleReferente.getChoralesFilles()) {
                locReturnList.addAll(locChorale.getParticipants());
            }
            locReturnList.addAll(_partNonAffilies);
        } else {
            for (final Participant locPart : ParticipantManager.getInstance().getAllData()) {
                if (_id.equals(locPart.getGroupeEvangelisationId())) {
                    locReturnList.add(locPart);
                }
            }
        }
        return locReturnList;
    }

    @Override
    public final List<String> exportDataToCSV() {
        final List<String> locResultList = new ArrayList<>();
        locResultList.add(_id);
        locResultList.add(_regions.toString());
        locResultList.add(_choraleReferente.getName());
        final StringBuilder locStringBuilder = new StringBuilder(64);
        for (final Participant locParticipant : getListParticipants()) {
            locStringBuilder.append(String.format("%s %s - ", locParticipant.getPrenom(), locParticipant.getNom()));
        }
        locResultList.add(locStringBuilder.toString());
        return locResultList;
    }

    public final List<List<String>> exportPartcipantGroupeERelation() {
        final List<List<String>> locResultList = new ArrayList<>();
        for (final Participant locPart : getListParticipants()) {
            final List<String> locPartList = new ArrayList<>(3);
//            locPartList.add(String.format("%s %s", _id, _choraleReferente.getName()));
            locPartList.add(_id);
            locPartList.add(locPart.getPrenom());
            locPartList.add(locPart.getNom());
            locPartList.add(locPart.getNumBillet());
            locResultList.add(locPartList);
        }
        return locResultList;
    }

    @Override
    public String toString() {
        return String.format(
                "Groupe E [%s] [%s] - Dept : %s - (limite %d) - %d participants (%s non affiliés à une chorale)",
                _id, _choraleReferente.getName(), _regions.toString(),
                _limite, getListParticipants().size(), _partNonAffilies.size()
        );
    }

    @Override
    public String getName() {
        return getId();
    }

    public final String getId() {
        return _id;
    }

    public final List<ARegion> getRegions() {
        return _regions;
    }

    public final void addRegion(final ARegion parRegion) {
        if (!_regions.contains(parRegion)) {
            _regions.add(parRegion);
        }
    }

    public final int getLimite() {
        return _limite;
    }

    public final void setLimite(final int parLimite) {
        this._limite = parLimite;
    }

    public final Chorale getChoraleReferente() {
        return _choraleReferente;
    }

    public final void setChoraleReferente(final Chorale parChoraleReferente) {
        this._choraleReferente = parChoraleReferente;
    }

    public final List<Participant> getPartNonAffilies() {
        return _partNonAffilies;
    }
}
