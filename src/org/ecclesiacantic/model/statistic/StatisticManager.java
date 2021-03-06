package org.ecclesiacantic.model.statistic;

import org.ecclesiacantic.gui.MessageProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class StatisticManager {

    static private StatisticManager _instance;

    static public final StatisticManager getInstance() {
        if (_instance == null) {
            _instance = new StatisticManager();
        }
        return _instance;
    }

    private final Map<EnumStatistics, AStatistic> _statistics;

    private StatisticManager() {
        _statistics = new HashMap<>();
        initAllStatistics();
    }

    public final void computeStatistics() {
        for (final AStatistic locStatistic : _statistics.values()) {
            if (!locStatistic.isStandaloneStatistic()) {
                locStatistic.computeStatistic();
            }
        }
    }

    public final void printResults() {
        for (final AStatistic locStatistic : _statistics.values()) {
            if (!locStatistic.isStandaloneStatistic()) {
                final String locDesc = String.format("\n%s", locStatistic.getDescription());
                System.out.println(locDesc);
                if (locStatistic.isToConsole()) {
                    MessageProvider.append(locDesc);
                }
                print(locStatistic);
            }
        }
    }

    public final void callStandaloneStatistic(final EnumStatistics parStandaloneStatistic) {
        if (!parStandaloneStatistic.isStandaloneStatistic()) {
            System.err.println(String.format("La statistique %s n'est pas de type standalone",
                    parStandaloneStatistic));
            return;
        }
        final AStatistic locStandaloneStatistic = _statistics.get(parStandaloneStatistic);
        locStandaloneStatistic.computeStatistic();
        print(locStandaloneStatistic);
    }

    private final void initAllStatistics() {
        for (final EnumStatistics locStaticType : EnumStatistics.values()) {
            final AStatistic locStat = constructClass(locStaticType);
            if (locStat != null) {
                locStat.setToConsole(locStaticType.isToConsole());
                _statistics.put(locStaticType, locStat);
            }
        }
    }

    private final AStatistic constructClass(final EnumStatistics parStatistic) {
        final String locClassPath = parStatistic.getClasspath();
        try {
            final Constructor<? extends AStatistic> locConstructor =
                    (Constructor<? extends AStatistic>) Class.forName(locClassPath).getConstructor(null);
            return locConstructor.newInstance();
        } catch (final ClassNotFoundException | InvocationTargetException | InstantiationException |
                IllegalAccessException | NoSuchMethodException parE) {
            parE.printStackTrace();
        }
        return null;
    }

    public final Map<EnumStatistics, AStatistic> getStatistics() {
        return _statistics;
    }

    private final void print(final AStatistic parStatistic) {
        final String locResult = parStatistic.printResult();
        System.out.println(locResult);
        if (parStatistic.isToConsole()) {
            MessageProvider.append(locResult);
        }
    }
}
