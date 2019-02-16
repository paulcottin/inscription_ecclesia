package org.ecclesiacantic.model.repartition;

import java.util.List;

public class RepartitionResult {

    private final List<List<String>> _participants, _badges;

    public RepartitionResult(final List<List<String>> parParticipants, final List<List<String>> parBadges) {
        _participants = parParticipants;
        _badges = parBadges;
    }

    public final List<List<String>> getParticipants() {
        return _participants;
    }

    public final List<List<String>> getBadges() {
        return _badges;
    }
}
