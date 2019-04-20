package org.ecclesiacantic.model.data;

import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;

import java.util.ArrayList;
import java.util.List;

public class GroupeConcert implements INamedObject {

    private final int _id;
    private final List<GroupeEvangelisation> _groupesEvangelisation;

    public GroupeConcert(final int parId) {
        this._id = parId;
        this._groupesEvangelisation = new ArrayList<>();
    }

    @Override
    public String getName() {
        return String.valueOf(_id);
    }

    public final List<Participant> getListParticipants() {
        final List<Participant> locReturnList = new ArrayList<>();
        for (final GroupeEvangelisation locGroupe : _groupesEvangelisation) {
            locReturnList.addAll(locGroupe.getListParticipants());
        }
        return locReturnList;
    }

    public final int getId() {
        return _id;
    }

    @Override
    public String toString() {
        final StringBuilder locStringBuilder = new StringBuilder();
        locStringBuilder.append("Id : ")
                .append(_id)
                .append(" (");
        for (final GroupeEvangelisation locGroupe : _groupesEvangelisation) {
            locStringBuilder.append(locGroupe.getId())
                    .append(", ");
        }
        locStringBuilder.append(")");
        return locStringBuilder.toString();
    }

    public final List<GroupeEvangelisation> getGroupesEvangelisation() {
        return _groupesEvangelisation;
    }

    public final void addGroupeEvangelisation(final GroupeEvangelisation parGroupe) {
        if (parGroupe != null && !_groupesEvangelisation.contains(parGroupe)) {
            _groupesEvangelisation.add(parGroupe);
        }
    }

    public final void computeNumBagagerie(final int parFirstNumber) {
        int locTmpNumBagagerieNumber = parFirstNumber;
        for (final GroupeEvangelisation locGroupe : _groupesEvangelisation) {
            for (final Participant locPart : locGroupe.getListParticipants()) {
                locPart.setNumBagagerie(locTmpNumBagagerieNumber);
                locTmpNumBagagerieNumber++;
            }
        }
    }
}
