package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.beans.participant.Chorale;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data.groupe_evangelisation.SoloGeographique;
import org.ecclesiacantic.model.data_manager.bean.ChoraleManager;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.utils.parser.CsvUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExportList;
import org.ecclesiacantic.utils.parser.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupeEvangelisationManager implements IExportableManager{

    // FIXME => passer en propriété
    static private final Pattern GROUPE_INFOS_FROM_STRING_PATTERN = Pattern.compile("(^[0-9]+)\\s+(.*)");

    static private GroupeEvangelisationManager _instance;

    static public final GroupeEvangelisationManager getInstance() {
        if (_instance == null) {
            _instance = new GroupeEvangelisationManager();
         }
         return _instance;
    }

    private final Map<String, GroupeEvangelisation> _dataMap;
    private final GroupeEvangelisation _nullData;

    public GroupeEvangelisationManager() {
        _dataMap = new HashMap<>();
        _nullData = new GroupeEvangelisation("-1", ChoraleManager.getInstance().getNullData());
    }

    private final void init() {
        for (final GroupeEvangelisation locGroupe : _dataMap.values()) {
            locGroupe.getPartNonAffilies().clear();
        }
    }

    private final void retrieveGroupeParticipant() {
        for (final Participant locPart : ParticipantManager.getInstance().getAllData()) {
            if (!locPart.isChante()) {
                continue;
            }
            final Matcher locMatcher = GROUPE_INFOS_FROM_STRING_PATTERN.matcher(locPart.getGroupeEvangelisationString());
            final String locGroupeId;
            if (locMatcher.matches()) {
                locGroupeId = locMatcher.group(1);
            } else {
                throw new IllegalArgumentException(String.format(
                        "Impossible de trouver le groupe et la chorale référente depuis les données '%s' du participant %s",
                        locPart.getGroupeEvangelisationString(), locPart
                ));
            }

            final Chorale locChorale = ChoraleManager.getInstance().get(locMatcher.group(2));
            if (locChorale == null) {
                throw new IllegalArgumentException(String.format(
                        "Impossible de trouver le groupe et la chorale référente depuis les données '%s' du participant %s",
                        locPart.getGroupeEvangelisationString(), locPart
                ));
            }

            if (!_dataMap.keySet().contains(locGroupeId)) {
                _dataMap.put(locGroupeId, new GroupeEvangelisation(locGroupeId, locChorale));
            }

            get(locGroupeId).getListParticipants().add(locPart);
        }
    }

    private final void computeGroupeParticipantFromRegionAndChoraleData() {
        //On crée d'abord les groupes en fonction des chorales référentes
        for (final Chorale locChorale : ChoraleManager.getInstance().getAllData()) {
            if (!locChorale.isReferente()) {
                continue;
            }
            _dataMap.put(locChorale.getIdGroupeEvangelisation(),
                    new GroupeEvangelisation(
                            locChorale.getIdGroupeEvangelisation(),
                            locChorale
                    )
            );
        }

        //Sinon on effectue le calcul pour savoir dans quel groupe va se retrouver le participant
        //On ajoute ensuite les solo geographiques
        for (final SoloGeographique locSoloGeographique : SoloGeographiqueManager.getInstance().getAllData()) {
            final GroupeEvangelisation locGroupe = get(locSoloGeographique.getIdGroupeEvangelisation());
            for (final Participant locSoloPart : SoloGeographiqueManager.getInstance().getSoloParticipantFrom(locSoloGeographique.getRegion())) {
                locGroupe.addParticipantNonAffilie(locSoloPart);
                locGroupe.addRegion(locSoloGeographique.getRegion());
            }
        }

        //On affecte aux participants leurs groupes d'évangelisation
        for (final GroupeEvangelisation locGroupe : _dataMap.values()) {
            final String locGroupeId = locGroupe.getId();
            for (final Participant locPart : locGroupe.getListParticipants()) {
                locPart.setGroupeEvangelisationId(locGroupeId);
            }
        }
    }

    public final void computeGroupeParticipant() {
        init();
        // Si le participant est dans une chorale alors il sera automatiquement pris en compte
        // lorsque l'on appelera la méthode getListParticipants()
        _dataMap.put(_nullData.getName(), _nullData);

        //Si on se contente de lire les infos chez les chorales et les participants
        if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
            computeGroupeParticipantFromRegionAndChoraleData();
        } else {
            retrieveGroupeParticipant();
        }
    }

    public final GroupeEvangelisation getNullData() {
        return _nullData;
    }

    public final GroupeEvangelisation get(final String parGroupeId) {
        if (parGroupeId != null && _dataMap.containsKey(parGroupeId)) {
            return _dataMap.get(parGroupeId);
        } else {
            System.err.println(String.format("Impossible de trouver le groupe d'évangélisation d'id %s",
                    parGroupeId));
            return null;
        }
    }

    @Override
    public final void exportDataToCSV() {
        final List<List<String>> locData = new ArrayList<>();
        locData.add(EnumDataColumnExportList.GROUPE_EVANGELISATION.asListString());
        for (final GroupeEvangelisation locExportableObj : _dataMap.values()) {
            locData.add(locExportableObj.exportDataToCSV());
        }

        //Génération du fichier
        final File locExportFile = EnumConfigProperty.OUTPUT_F_G_EVAN.fileV();
        try {
            CsvUtils.export(locExportFile, locData);
        } catch (final IOException parE) {
            parE.printStackTrace();
        }
    }

    public final void exportParticipantGroupeERelation() {
        final List<List<String>> locData = new ArrayList<>();
        locData.add(EnumDataColumnExportList.GROUPE_EVANGELISATION_PARTICIPANT_RELATION.asListString());
        for (final GroupeEvangelisation locGroupe : _dataMap.values()) {
            locData.addAll(locGroupe.exportPartcipantGroupeERelation());
        }

        final File locExportFile = EnumConfigProperty.OUTPUT_F_G_EVAN_PART_RELATION.fileV();
        try {
            CsvUtils.export(locExportFile, locData);
        } catch (final IOException parE) {
            parE.printStackTrace();
        }
    }

    public final void printMailingList() {
        final StringBuilder locStringBuilder = new StringBuilder();
        for (final GroupeEvangelisation locGroupe : _dataMap.values()) {
            if (locGroupe.getChoraleReferente().equals(ChoraleManager.getInstance().getNullData())) {
                continue;
            }
            locStringBuilder.append(String.format("%s : %s\r\n",
                    locGroupe.getId(),
                    locGroupe.getChoraleReferente().getName()));
            for (final Participant locParticipant : locGroupe.getListParticipants()) {
                locStringBuilder.append(locParticipant.getEmail());
                locStringBuilder.append(";");
            }
            locStringBuilder.append("\r\n\r\n");
        }
        final File locOutputFile = EnumConfigProperty.OUTPUT_F_MAILING_L.fileV();
        FileUtils.write(locOutputFile, locStringBuilder.toString());
    }

    public final Map<String, GroupeEvangelisation> getAllData() {
        return _dataMap;
    }
}
