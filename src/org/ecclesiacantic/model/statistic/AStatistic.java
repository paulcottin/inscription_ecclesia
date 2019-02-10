package org.ecclesiacantic.model.statistic;

public abstract class AStatistic {

    private final String _description;
    private final boolean _isStandalone;

    public AStatistic(final String parDescription, final boolean parIsStandalone) {
        _description = parDescription;
        _isStandalone = parIsStandalone;
    }

    public AStatistic(final String parDescription) {
        this(parDescription, false);
    }

    public abstract void computeStatistic();
    public abstract void printResult();

    public final String getDescription() {
        return _description;
    }

    public final  boolean isStandaloneStatistic() {
        return _isStandalone;
    }
}
