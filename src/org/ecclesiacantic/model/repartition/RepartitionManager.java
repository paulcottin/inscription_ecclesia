package org.ecclesiacantic.model.repartition;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data_manager.BadgeManager;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.model.data_manager.GroupeConcertManager;
import org.ecclesiacantic.model.data_manager.GroupeEvangelisationManager;
import org.ecclesiacantic.model.statistic.EnumStatistics;
import org.ecclesiacantic.model.statistic.StatisticManager;
import org.ecclesiacantic.model.statistic.statistics.OccupationCreneauStatistic;
import org.ecclesiacantic.utils.parser.FileUtils;

import java.util.*;

public class RepartitionManager {

    static private RepartitionManager _instance;

    static public final RepartitionManager getInstance() {
        if (_instance == null) {
            _instance = new RepartitionManager();
        }
        return _instance;
    }

    private final double _minFullFactor, _maxFullFactor;
    private final int _minLessUsedPartNb, _maxLessUsedPartNb;
    private final List<Repartition> _repartitionList;
    private final StringBuilder _repartitionResult;
    private final Map<Integer, List<List<String>>> _repartitionResultMap, _bagesDataMap;

    private RepartitionManager() {
        _repartitionList = new ArrayList<>();
        _repartitionResult = new StringBuilder();
        _minFullFactor = EnumConfigProperty.MIN_FULL_FACTOR.doubleV();
        _maxFullFactor = EnumConfigProperty.MAX_FULL_FACTOR.doubleV();
        _minLessUsedPartNb = EnumConfigProperty.LESS_USED_PART.intV();
        _maxLessUsedPartNb = EnumConfigProperty.MAX_USED_PART.intV();
        _repartitionResultMap = new HashMap<>();
        _bagesDataMap = new HashMap<>();
    }

    private final void computeRepartition() {
        for (int locMcNb = _minLessUsedPartNb; locMcNb <= _maxLessUsedPartNb; locMcNb++) {
            for (double locI = _minFullFactor; locI <= _maxFullFactor; locI += 0.05) {
                _repartitionList.add(new Repartition(locI, locMcNb));
            }
        }
    }

    public final void runRepartitions() {
        computeRepartition();

        int locIndex = 0;
        for (final Repartition locRepartition : _repartitionList) {
            locIndex++;
            locRepartition.run();
            _repartitionResult.append(String.format("Répartition numéro %d : fullFactor = %s et lessUsedNb = %d\n",
                    locIndex, locRepartition.getFullFactor(), locRepartition.getLessUsedMcNumber())
            ).append(String.format("\tSomme des écarts types : %s, nb de personnes ne faisant rien à un créneau : %d\n",
                    EvenementManager.getInstance().getSommeEcartType(),
                    ((OccupationCreneauStatistic) StatisticManager.getInstance().getStatistics().get(EnumStatistics.OCCUPATION_CRENEAUX)).getErrorPartSet().size())
            );
            _repartitionResultMap.put(locIndex, EvenementManager.getInstance().exportSallePopulation(false));
            exportBadgesData();
            BadgeManager.getInstance().generateBadges();
            _bagesDataMap.put(locIndex, BadgeManager.getInstance().exportDataToCSV(false));
        }
    }

    public final void printResult() {
        _repartitionResult.append("\n\nQuel résultat voulez-vous sauvegarder ?\n");
        System.out.println(_repartitionResult.toString());

        final Scanner locScanner = new Scanner(System.in);
        final String locIdData = locScanner.next();

        System.out.println(String.format("Enregistrement des données d'id %s", locIdData));

        FileUtils.writeCsv(EnumConfigProperty.OUTPUT_F_NB_PART_BY_CRENEAU.fileV(), _repartitionResultMap.get(Integer.parseInt(locIdData)));
        FileUtils.writeCsv(EnumConfigProperty.OUTPUT_F_BADGE.fileV(), _bagesDataMap.get(Integer.parseInt(locIdData)));
    }

    public final void exportBadgesData() {
        GroupeEvangelisationManager.getInstance().computeGroupeParticipant();

        GroupeConcertManager.getInstance().parseDataFile();
        GroupeConcertManager.getInstance().computeNumBagagerie();

        BadgeManager.getInstance().generateBadges();
    }
}
