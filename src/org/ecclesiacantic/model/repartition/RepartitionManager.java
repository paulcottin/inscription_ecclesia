package org.ecclesiacantic.model.repartition;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.participant.Participant;
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
    private final Map<Integer, Repartition> _repartitions;

    private int _selectedIdx;

    private RepartitionManager() {
        _repartitionList = new ArrayList<>();
        _repartitionResult = new StringBuilder();
        _minFullFactor = EnumConfigProperty.MIN_FULL_FACTOR.doubleV();
        _maxFullFactor = EnumConfigProperty.MAX_FULL_FACTOR.doubleV();
        _minLessUsedPartNb = EnumConfigProperty.LESS_USED_PART.intV();
        _maxLessUsedPartNb = EnumConfigProperty.MAX_USED_PART.intV();
        _repartitions = new HashMap<>();
        _selectedIdx = -1;
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
            _repartitions.put(locIndex, locRepartition);
            final Set<Participant> locNotOccupiedParticipants = ((OccupationCreneauStatistic) StatisticManager.getInstance().getStatistics().get(EnumStatistics.OCCUPATION_CRENEAUX)).getErrorPartSet();
            _repartitionResult.append(String.format("Répartition numéro %d : fullFactor = %s et lessUsedNb = %d\n",
                    locIndex, locRepartition.getFullFactor(), locRepartition.getLessUsedMcNumber())
            ).append(String.format("\tSomme des écarts types : %s, nb de personnes ne faisant rien à un créneau : %d\n",
                    EvenementManager.getInstance().getSommeEcartType(),
                    locNotOccupiedParticipants.size())
            );
            exportBadgesData();
            locRepartition.setResult(new RepartitionResult(
                    EvenementManager.getInstance().exportSallePopulation(false),
                    BadgeManager.getInstance().exportDataToCSV(false),
                    locNotOccupiedParticipants));
        }
    }

    public final void printResult() {
        _repartitionResult.append("\n\nQuel résultat voulez-vous sauvegarder ?\n");
        System.out.println(_repartitionResult.toString());

        final Scanner locScanner = new Scanner(System.in);
        final String locIdData = locScanner.next();
        _selectedIdx = Integer.valueOf(locIdData);

        System.out.println(String.format("Enregistrement des données d'id %s", _selectedIdx));

        final RepartitionResult locSelectedResult = _repartitions.get(_selectedIdx).getResult();

        FileUtils.writeCsv(EnumConfigProperty.OUTPUT_F_NB_PART_BY_CRENEAU.fileV(), locSelectedResult.getParticipants());
        FileUtils.writeCsv(EnumConfigProperty.OUTPUT_F_BADGE.fileV(), locSelectedResult.getBadges());

        System.out.println("Affichage des participants non occupés à au moins un créneau : ");
        for (final Participant locParticipant : locSelectedResult.getParticipantNotOccupiedOneCreneauSet()) {
            System.out.println(computeNotOccupiedPartOutput(locParticipant));
        }
    }

    private final String computeNotOccupiedPartOutput(final Participant parParticipant) {
        final StringBuilder locBuilder = new StringBuilder(String.format("\t- %s", parParticipant))
                .append("\n\t\tVoeux : \n");
        for (final MasterClass locVoeu : parParticipant.getVoeux()) {
            locBuilder.append("\t\t\t- ").append(locVoeu.getName()).append("\n");
        }
        locBuilder.append("\n\t\tEvénements : \n");
        for (final Evenement locEvt : parParticipant.getEvenements()) {
            locBuilder.append("\t\t\t- ").append(locEvt).append("\n");
        }
        return locBuilder.toString();
    }

    public final void exportBadgesData() {
        GroupeEvangelisationManager.getInstance().computeGroupeParticipant();

        GroupeConcertManager.getInstance().parseDataFile();
        GroupeConcertManager.getInstance().computeNumBagagerie();

        BadgeManager.getInstance().generateBadges();
    }
}