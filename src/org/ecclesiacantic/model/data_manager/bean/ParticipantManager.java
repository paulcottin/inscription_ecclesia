package org.ecclesiacantic.model.data_manager.bean;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.participant.*;
import org.ecclesiacantic.model.data.groupe_evangelisation.ARegion;
import org.ecclesiacantic.model.data.groupe_evangelisation.RegionManager;
import org.ecclesiacantic.model.data_manager.ADataManager;
import org.ecclesiacantic.utils.CompareUtils;
import org.ecclesiacantic.utils.EnumUtils;
import org.ecclesiacantic.utils.parser.CsvUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.FileUtils;
import org.ecclesiacantic.utils.parser.NumberUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParticipantManager extends ADataManager<Participant> {

    static private ParticipantManager _instance;
    static public final ParticipantManager getInstance() {
        if (_instance == null) {
            _instance = new ParticipantManager();
        }
        return _instance;
    }

    private final Map<MasterClass, Integer> _masterClassPopularityMap3Voeux, _mcPopularityMapAllVoeux;
    private final List<Participant> _tempToRemovePartList;

    private ParticipantManager() {
        super(EnumConfigProperty.INPUT_F_PARTICIPANT, EnumDataType.PARTICIPANT);
        _masterClassPopularityMap3Voeux = new HashMap<>();
        _mcPopularityMapAllVoeux = new HashMap<>();
        _tempToRemovePartList = new ArrayList<>();
        reset();
    }

    public final List<Participant> getParticipantChantantList() {
        final List<Participant> locReturnList = new ArrayList<>();
        for (final Participant locParticipant : _dataMap.values()) {
            if (locParticipant.isChante()) {
                locReturnList.add(locParticipant);
            }
        }
        return locReturnList;
    }

    @Override
    public void preProcessingDataFile() {
        tempProcessingDataFile(_propertyDataFile);
    }

    public final void tempProcessingDataFile(final File parFile) {
        //#TODO : A enlever après que les modifs aient été appliquées sur le drive
        //Modification du champ Chorale en Chorale_1
        String locFileContent = FileUtils.extractString(parFile);

        FileUtils.write(parFile, locFileContent);

        CsvUtils.cleanEmptyCsvLines(parFile);
    }

    public final Map<MasterClass, Integer> getAllPartChoicesStat() {
        return getXFirstPartChoicesStat(EnumConfigProperty.NB_MAX_VOEUX.intV());
    }

    public final Map<MasterClass, Integer> getToConsiderFirstPartChoicesStat() {
        return getXFirstPartChoicesStat(EnumConfigProperty.NB_VOEUX_TO_CONSIDER.intV());
    }

    private final Map<MasterClass, Integer> getXFirstPartChoicesStat(final int parNumberVoeuxToConsider) {
        final int locNumberVoeuxToConsider = parNumberVoeuxToConsider > EnumConfigProperty.NB_MAX_VOEUX.intV() ? EnumConfigProperty.NB_MAX_VOEUX.intV() : parNumberVoeuxToConsider;
        final Map<MasterClass, Integer> locReturnMap = new HashMap<>();
        // init
        for (final MasterClass locMc : MasterClassManager.getInstance().getAllData()) {
            locReturnMap.put(locMc, 0);
        }

        int locMcCpt;
        for (final Participant locPart : ParticipantManager.getInstance().getAllData()) {
            for (int locIndex = 0; locIndex < locNumberVoeuxToConsider; locIndex++) {
                locMcCpt = locReturnMap.get(locPart.getVoeu(locIndex));
                locReturnMap.put(locPart.getVoeu(locIndex), ++locMcCpt);
            }
        }
        return locReturnMap;
    }

    public final int getToConsiderFirstPartChoicesStat(final MasterClass parMasterclass) {
        if (_masterClassPopularityMap3Voeux.isEmpty()) {
            _masterClassPopularityMap3Voeux.putAll(getToConsiderFirstPartChoicesStat());
        }
        return _masterClassPopularityMap3Voeux.get(parMasterclass);
    }

    public final int getAllPartChoicesStat(final MasterClass parMasterclass) {
        if (_mcPopularityMapAllVoeux.isEmpty()) {
            _mcPopularityMapAllVoeux.putAll(getAllPartChoicesStat());
        }
        return _mcPopularityMapAllVoeux.get(parMasterclass);
    }

    @Override
    public void reset() {
        _masterClassPopularityMap3Voeux.clear();
        _mcPopularityMapAllVoeux.clear();
        _tempToRemovePartList.clear();
        for (final Participant locPart : _dataMap.values()) {
            locPart.getEvenements().clear();
        }
    }

    @Override
    protected Participant convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) {
        //Propriétés objets
        final EnumCivilite locCivilite = EnumCivilite.computeFromName(parStringMapHeaderValue.get(EnumDataColumImport.P_CIVILITE));
        final Tarif locTarif = TarifManager.getInstance().get(parStringMapHeaderValue.get(EnumDataType.TARIF.getHeaderId()));
        final Hebergement locHebergement = new Hebergement(
                parStringMapHeaderValue.get(EnumDataColumImport.P_STATION_METRO),
                (EnumSexeHebergement) EnumUtils.getEnumFromString(EnumSexeHebergement.class, parStringMapHeaderValue.get(EnumDataColumImport.P_PEUT_ACCUEILLIR)),
                parStringMapHeaderValue.get(EnumDataColumImport.P_PEUT_HERBERGER)
        );
        final Pays locPays = PaysManager.getInstance().get(
                parStringMapHeaderValue.get(EnumDataColumImport.P_PAYS)
        );
        final ARegion locRegion = RegionManager.getInstance().getRegion(
                parStringMapHeaderValue.get(EnumDataColumImport.P_CODE_POSTAL),
                locPays
        );
        final LinkedList<MasterClass> locVoeux = new LinkedList<>();
        final MasterClassManager locMcMan = MasterClassManager.getInstance();
        locVoeux.add(0, locMcMan.get(parStringMapHeaderValue.get(EnumDataColumImport.P_VOEU1)));
        locVoeux.add(1, locMcMan.get(parStringMapHeaderValue.get(EnumDataColumImport.P_VOEU2)));
        locVoeux.add(2, locMcMan.get(parStringMapHeaderValue.get(EnumDataColumImport.P_VOEU3)));
        locVoeux.add(3, locMcMan.get(parStringMapHeaderValue.get(EnumDataColumImport.P_VOEU4)));
        locVoeux.add(4, locMcMan.get(parStringMapHeaderValue.get(EnumDataColumImport.P_VOEU5)));

        final EnumPupitre locPupitre = (EnumPupitre) EnumUtils.getEnumFromString(EnumPupitre.class, parStringMapHeaderValue.get(EnumDataColumImport.P_PUPITRE));
        final Chorale locChorale = ChoraleManager.getInstance().get(parStringMapHeaderValue.get(EnumDataColumImport.P_CHORALE));
        final Map<EnumConnaitPar, Boolean> locConnaitPar = EnumConnaitPar.initFromData(parStringMapHeaderValue);

        //Propriete calculées
        Date locNaissanceDate = null, locCommandeDate = null;

        final SimpleDateFormat locDateNaissanceFormat = new SimpleDateFormat("dd/mm/YYYY");
        final SimpleDateFormat locDateCommandeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        try {
            locNaissanceDate = locDateNaissanceFormat.parse(
                    parStringMapHeaderValue.get(EnumDataColumImport.P_DATE_NAISSANCE)
            );
            if (parStringMapHeaderValue.get(EnumDataColumImport.P_DATECOMMANDE) == null) {
                locCommandeDate = new Date(0);
            } else {
                locCommandeDate = locDateCommandeFormat.parse(String.format("%s %s",
                        parStringMapHeaderValue.get(EnumDataColumImport.P_DATECOMMANDE),
                        parStringMapHeaderValue.get(EnumDataColumImport.P_HEURECOMMANDE)
                ));
            }

        } catch (final ParseException parE) {
            System.err.println("Erreur lors de la création des dates : %s");
            parE.printStackTrace();
        }

        return new Participant(
                parStringMapHeaderValue.get(EnumDataColumImport.P_PRENOM),
                parStringMapHeaderValue.get(EnumDataColumImport.P_NOM),
                locCivilite,
                locTarif,
                parStringMapHeaderValue.get(EnumDataColumImport.P_CODEBARRE),
                parStringMapHeaderValue.get(EnumDataColumImport.P_NUMERO_BILLET),
                parStringMapHeaderValue.get(EnumDataColumImport.P_EMAIL),
                parStringMapHeaderValue.get(EnumDataColumImport.P_TELEPHONE),
                locRegion,
                parStringMapHeaderValue.get(EnumDataColumImport.P_CODE_POSTAL),
                locCommandeDate,
                locNaissanceDate,
                NumberUtils.convertFieldToInt(parStringMapHeaderValue.get(EnumDataColumImport.P_AGE)),
                parStringMapHeaderValue.get(EnumDataColumImport.P_AUTRES_INFOS),
                parStringMapHeaderValue.get(EnumDataColumImport.P_MESSAGE),
                parStringMapHeaderValue.get(EnumDataColumImport.DIOCESE),
                CompareUtils.isMarkTrue(parStringMapHeaderValue.get(EnumDataColumImport.P_BESOIN_HEBERGEMENT)),
                CompareUtils.isMarkTrue(parStringMapHeaderValue.get(EnumDataColumImport.P_PEUT_HERBERGER)),
                CompareUtils.isMarkTrue(parStringMapHeaderValue.get(EnumDataColumImport.P_NE_SOUHAITE_PAS_CHANTER)),
                CompareUtils.isMarkTrue(parStringMapHeaderValue.get(EnumDataColumImport.IS_PARTICIPANT_2016)),
                CompareUtils.isMarkTrue(parStringMapHeaderValue.get(EnumDataColumImport.AIDE_OFFICES)),
                locHebergement,
                locVoeux,
                locPupitre,
                locChorale,
                locConnaitPar,
                parStringMapHeaderValue.get(EnumDataColumImport.P_GROUPE_EVANGELISATION),
                parStringMapHeaderValue.get(EnumDataColumImport.P_GROUPE_CONCERT)
        );
    }

    public final List<Participant> getTempToRemovePartList() {
        return _tempToRemovePartList;
    }
}
