package org.ecclesiacantic.model.statistic;

public abstract class AStatistic {

    private final String _description;
    private final boolean _isStandalone;
    private boolean _toConsole;

    public AStatistic(final String parDescription, final boolean parIsStandalone) {
        _description = parDescription;
        _isStandalone = parIsStandalone;
        _toConsole = false;
    }

    public AStatistic(final String parDescription) {
        this(parDescription, false);
    }

    public abstract void computeStatistic();
    public abstract String printResult();

    public final String getDescription() {
        return _description;
    }

    public final  boolean isStandaloneStatistic() {
        return _isStandalone;
    }

    public final boolean isToConsole() {
        return _toConsole;
    }

    public void setToConsole(final boolean parToConsole) {
        _toConsole = parToConsole;
    }
}
