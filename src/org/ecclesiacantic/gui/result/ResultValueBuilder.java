package org.ecclesiacantic.gui.result;

import org.ecclesiacantic.model.repartition.RepartitionResult;

import java.util.ArrayList;
import java.util.Collection;

public class ResultValueBuilder {

    private final Collection<RepartitionResult> _results;

    public ResultValueBuilder(final Collection<RepartitionResult> parRepartitionResults) {
        _results = parRepartitionResults;
    }

    public final Collection<ResultValue> build() {
        final Collection<ResultValue> locValues = new ArrayList<>(_results.size());
        for (final RepartitionResult locResult : _results) {
            locValues.add(new ResultValue(locResult.getFullFactor(),
                    locResult.getSommeEcartType(),
                    locResult.getLessUsedNb(),
                    locResult.getParticipantNotOccupiedOneCreneauSet().size()));
        }
        return locValues;
    }
}
