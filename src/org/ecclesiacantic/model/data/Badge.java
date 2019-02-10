package org.ecclesiacantic.model.data;

import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data.beans.participant.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Badge {

    private final Participant _participant;
    private final Map<EnumCreneau, Evenement> _evenements;

    public Badge(final Participant parParticipant, final Map<EnumCreneau, Evenement> parEvenements) {
        this._participant = parParticipant;
        this._evenements = parEvenements;
    }

    @Override
    public String toString() {
        final StringBuilder locStringBuilder = new StringBuilder(128);
        locStringBuilder.append(String.format("%s %s", _participant.getPrenom(), _participant.getNom()))
                .append(String.format("g. évang : %s, g. conc : %d", _participant.getGroupeEvangelisationId(),
                        _participant.getGroupeConcertId()))
                .append(_evenements.toString());
        return locStringBuilder.toString();
    }

    public final List<String> exportToCSV(){
        final List<String> locReturnList = new ArrayList<>();
        locReturnList.add(_participant.getPrenom());
        locReturnList.add(_participant.getNom());
        locReturnList.add(_participant.getNumBillet());
        locReturnList.add(String.valueOf(_participant.getNumBagagerie()));
        for (final EnumCreneau locCreneau : EnumCreneau.values()) {
            final Evenement locEvt = _participant.getEvenement(locCreneau);
            if (locEvt != null) {
                if (locEvt.getMasterclass().getDiviserEn() == 1) {
                    locReturnList.add(String.format("%s-%s", locEvt.getMasterclass().getId(), locEvt.getSalleList().get(0).getRepere()));
                } else {
                    final Salle locSalleForPart = locEvt.getSalleFor(_participant);
                    locReturnList.add(String.format("%s.%s-%s",
                            locEvt.getMasterclass().getId(),
                            locEvt.getSalleList().indexOf(locSalleForPart) + 1,
                            locSalleForPart != null ? locSalleForPart.getRepere() : String.format("Aucune salle pour le Participant %s", _participant))
                    );
                }
            } else {
                locReturnList.add("");
            }
        }
        if (_participant.isChante()) {
            locReturnList.add(_participant.getGroupeEvangelisationId());
            locReturnList.add(String.valueOf(_participant.getGroupeConcertId()));
        } else {
            locReturnList.add("");
            locReturnList.add("");
        }

        return locReturnList;
    }

    public final Participant getParticipant() {
        return _participant;
    }

    public final Map<EnumCreneau, Evenement> getEvenements() {
        return _evenements;
    }
}
