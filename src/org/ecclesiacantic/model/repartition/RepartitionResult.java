package org.ecclesiacantic.model.repartition;

import org.ecclesiacantic.model.data.beans.participant.Participant;

import java.util.List;
import java.util.Set;

public class RepartitionResult {

    private final List<List<String>> _participants, _badges;
    private final Set<Participant> _participantNotOccupiedOneCreneauSet;

    public RepartitionResult(final List<List<String>> parParticipants, final List<List<String>> parBadges, Set<Participant> participantNotOccupiedOneCreneauSet) {
        _participants = parParticipants;
        _badges = parBadges;
        _participantNotOccupiedOneCreneauSet = participantNotOccupiedOneCreneauSet;
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
}
