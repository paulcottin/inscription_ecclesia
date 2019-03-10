package org.ecclesiacantic.model.repartition;

import org.ecclesiacantic.model.data.beans.participant.Participant;

import java.util.List;
import java.util.Set;

public class RepartitionResult {

    private final List<List<String>> _participants, _badges;
    private final Set<Participant> _participantNotOccupiedOneCreneauSet;
    private final int _lessUsedNb;
    private final double _fullFactor, _sommeEcartType;

    public RepartitionResult(final List<List<String>> parParticipants, final List<List<String>> parBadges, Set<Participant> participantNotOccupiedOneCreneauSet,
                             final double parFullFactor, final int parLessUsedNb, final double parSommeEcartType) {
        _participants = parParticipants;
        _badges = parBadges;
        _participantNotOccupiedOneCreneauSet = participantNotOccupiedOneCreneauSet;
        _fullFactor = parFullFactor;
        _lessUsedNb = parLessUsedNb;
        _sommeEcartType = parSommeEcartType;
    }

    public final List<List<String>> getParticipants() {
        return _participants;
    }

    public final List<List<String>> getBadges() {
        return _badges;
    }

    public final Set<Participant> getParticipantNotOccupiedOneCreneauSet() {
        return _participantNotOccupiedOneCreneauSet;
    }

    public int getLessUsedNb() {
        return _lessUsedNb;
    }

    public double getFullFactor() {
        return _fullFactor;
    }

    public double getSommeEcartType() {
        return _sommeEcartType;
    }
}
