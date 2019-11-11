package org.ecclesiacantic.model.statistic.statistics;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.statistic.AStatistic;
import org.ecclesiacantic.model.statistic.EnumStatistics;
import org.ecclesiacantic.model.statistic.StatisticManager;
import org.ecclesiacantic.utils.CompareUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExportList;
import org.ecclesiacantic.utils.parser.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OccupationCreneauStatistic extends AStatistic {

    static private final String ERROR_FILE_NAME = "err_participant_rien_prevu.Csv";

    private final StringBuilder _errorStrB;
    private int _errorCpt;
    private final Set<Participant> _errorPartSet;

    public OccupationCreneauStatistic() {
        super("Vérification que chaque participant a une occupation à un créneau donné");
        _errorStrB = new StringBuilder();
        _errorCpt = 0;
        _errorPartSet = new HashSet<>();
    }

    @Override
    public void computeStatistic() {
        resetStatistic();
        final boolean locSkipPartMalformedVoeux = EnumConfigProperty.IS_SKIP_MALFORMED_VOEUX.boolV();
        final Set<Participant> locMalformedListVoeux = ((CheckVoeuxDifferentsParParticipantStatistic) StatisticManager.getInstance().getStatistics().get(EnumStatistics.SAME_VOEUX_FOR_PARTICIPANT)).getCptVoeux().keySet();
        final List<List<String>> locData = new ArrayList<>();
        locData.add(EnumDataColumnExportList.PARTICIPANT_NE_FAIT_RIEN_PENDANT_CRENEAU.asListString());
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            if (locSkipPartMalformedVoeux && locMalformedListVoeux.contains(locParticipant)) {
                continue;
            }
            for (final EnumCreneau locCreneau : EnumCreneau.values()) {
                final List<String> locDataLine = new ArrayList<>();
                if (locParticipant.getEvenement(locCreneau) == null) {
                    _errorStrB.append(String.format("\tLe participant %s n'a rien de prévu pendant le %s\n\t\tRépartition : %s\n\t\tVoeux : %s\n",
                            locParticipant,
                            locCreneau,
                            locParticipant.getEvenements(),
                            locParticipant.getVoeux())
                    );
                    _errorPartSet.add(locParticipant);
                    locDataLine.add(locParticipant.getNumBillet());
                    locDataLine.add(locParticipant.getEmail());
                    locDataLine.add(locCreneau.toString());
                    for (final Object locNotAttribuedMc : CompareUtils.exclude(locParticipant.getVoeux(), locParticipant.getAttribuedMasterClasses())) {
                        locDataLine.add(locNotAttribuedMc.toString());
                    }
                    locData.add(locDataLine);
                    _errorCpt++;
                }
            }
        }

        final File locOutputDir = EnumConfigProperty.OUTPUT_F_BADGE.fileV().getAbsoluteFile().getParentFile();
        FileUtils.writeCsv(new File(locOutputDir, ERROR_FILE_NAME), locData);
    }

    @Override
    public String printResult() {
        if (_errorStrB.length() == 0) {
            return "OK";
        } else {
            return String.format("Nombre d'erreurs : %d\n%s", _errorCpt, _errorStrB.toString());
        }
    }

    public final Set<Participant> getErrorPartSet() {
        if (_errorPartSet.size() == 0) {
            computeStatistic();
        }
        return _errorPartSet;
    }

    public final void resetStatistic() {
        _errorPartSet.clear();
        _errorCpt = 0;
        _errorStrB.setLength(0);
    }
}
