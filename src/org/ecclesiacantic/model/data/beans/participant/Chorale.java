package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data_manager.bean.ChoraleManager;

import java.util.ArrayList;
import java.util.List;

public class Chorale implements INamedObject {

    final private String _name;
    final private boolean _referente;
    final private String _idGroupeEvangelisation;
    final List<Chorale> _choralesFilles;
    final List<Participant> _participants;

    public Chorale(final String parName, final boolean parIsReferente,
                   final String parGroupeEvangelisationId) {
        _name = parName;
        _referente = parIsReferente;
        _choralesFilles = new ArrayList<>();
        _participants = new ArrayList<>();
        _idGroupeEvangelisation = parGroupeEvangelisationId;
    }

    public final void addParticipant(final Participant parParticipant) {
        if (!parParticipant.isChante() || !parParticipant.isChoraleAffilie()) {
            return;
        }
        if (_participants.contains(parParticipant)) {
            System.err.println(String.format("Le participant %s est déjà inscrit dans la chorale %s",
                    parParticipant.toString(),
                    this.toString()));
        } else {
            _participants.add(parParticipant);
        }
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
        if (_participants.isEmpty()) {
            ChoraleManager.getInstance().computeParticipantsInChorales();
        }
        return _participants;
    }

    public final String getIdGroupeEvangelisation() {
        return _idGroupeEvangelisation;
    }
}
