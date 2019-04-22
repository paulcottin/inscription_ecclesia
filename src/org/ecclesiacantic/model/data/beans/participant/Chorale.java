package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chorale implements INamedObject {

    final private String _name;
    final private boolean _referente;
    final private String _idGroupeEvangelisation;
    private final List<Chorale> _choralesFilles;

    public Chorale(final String parName, final boolean parIsReferente,
                   final String parGroupeEvangelisationId) {
        _name = parName;
        _referente = parIsReferente;
        _choralesFilles = new ArrayList<>();
        _idGroupeEvangelisation = parGroupeEvangelisationId;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public final boolean isReferente() {
        return _referente;
    }

    public final List<Chorale> getChoralesFilles() {
        return _choralesFilles;
    }

    public final void addChoraleFille(final Chorale parChorale) {
        if (parChorale != null) {
            _choralesFilles.add(parChorale);
        } else {
            System.err.println(String.format("Impossible d'ajouter une chorale null à la chorale référente %s",
                    this.toString()
            ));
        }
    }

    public final List<Participant> getParticipants() {
        final List<Participant> locParticipants = new ArrayList<>();
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            final Chorale locChorale = locParticipant.getChorale();
            if (locChorale != null && Objects.equals(locChorale.getName(), _name)) {
                locParticipants.add(locParticipant);
            }
        }
        return locParticipants;
    }

    public final String getIdGroupeEvangelisation() {
        return _idGroupeEvangelisation;
    }
}
