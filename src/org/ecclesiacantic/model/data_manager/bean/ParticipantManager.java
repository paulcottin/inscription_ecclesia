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
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    protected Participant convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) throws ObjectInstanciationException {
        final String locEmail = stringV(parStringMapHeaderValue,EnumDataColumImport.P_EMAIL);
        //Propriétés objets
        final EnumCivilite locCivilite = EnumCivilite.computeFromName(stringV(parStringMapHeaderValue, EnumDataColumImport.P_CIVILITE));
        if (locCivilite == null) {
            throw new ObjectInstanciationException(_typeName, EnumDataColumImport.P_CIVILITE, locEmail,
                    Arrays.stream(EnumCivilite.values()).map(EnumCivilite::getValue).collect(Collectors.toList()));
        }
        final Tarif locTarif = TarifManager.getInstance().get(stringV(parStringMapHeaderValue, EnumDataType.TARIF));
        final Hebergement locHebergement = new Hebergement(
                stringV(parStringMapHeaderValue, EnumDataColumImport.P_STATION_METRO),
                (EnumSexeHebergement) EnumUtils.getEnumFromString(EnumSexeHebergement.class, stringV(parStringMapHeaderValue,EnumDataColumImport.P_PEUT_ACCUEILLIR)),
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_PEUT_HERBERGER)
        );
        final Pays locPays = PaysManager.getInstance().get(
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_PAYS)
        );
        final ARegion locRegion = RegionManager.getInstance().getRegion(
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_CODE_POSTAL),
                locPays
        );
        final LinkedList<MasterClass> locVoeux = new LinkedList<>();
        final MasterClassManager locMcMan = MasterClassManager.getInstance();
        locVoeux.add(0, locMcMan.get(stringV(parStringMapHeaderValue,EnumDataColumImport.P_VOEU1)));
        locVoeux.add(1, locMcMan.get(stringV(parStringMapHeaderValue,EnumDataColumImport.P_VOEU2)));
        locVoeux.add(2, locMcMan.get(stringV(parStringMapHeaderValue,EnumDataColumImport.P_VOEU3)));
        locVoeux.add(3, locMcMan.get(stringV(parStringMapHeaderValue,EnumDataColumImport.P_VOEU4)));
        locVoeux.add(4, locMcMan.get(stringV(parStringMapHeaderValue,EnumDataColumImport.P_VOEU5)));

        final EnumPupitre locPupitre = (EnumPupitre) EnumUtils.getEnumFromString(EnumPupitre.class, stringV(parStringMapHeaderValue,EnumDataColumImport.P_PUPITRE));
        if (locPupitre == null) {
            throw new ObjectInstanciationException(_typeName, EnumDataColumImport.P_PUPITRE, locEmail,
                    Arrays.stream(EnumPupitre.values()).map(EnumPupitre::getValue).collect(Collectors.toList()));
        }
        final Chorale locChorale = ChoraleManager.getInstance().get(stringV(parStringMapHeaderValue,EnumDataColumImport.P_CHORALE));
        final Map<EnumConnaitPar, Boolean> locConnaitPar = EnumConnaitPar.initFromData(parStringMapHeaderValue);

        //Propriete calculées
        Date locNaissanceDate = null, locCommandeDate = null;

        final SimpleDateFormat locDateNaissanceFormat = new SimpleDateFormat("dd/mm/YYYY");
        final SimpleDateFormat locDateCommandeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        locNaissanceDate = dateV(parStringMapHeaderValue, EnumDataColumImport.P_DATE_NAISSANCE, locDateNaissanceFormat);
        locCommandeDate = dateV(parStringMapHeaderValue, EnumDataColumImport.P_DATECOMMANDE, locDateCommandeFormat);

        return new Participant(
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_PRENOM),
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_NOM),
                locCivilite,
                locTarif,
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_CODEBARRE),
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_NUMERO_BILLET),
                locEmail,
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_TELEPHONE),
                locRegion,
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_CODE_POSTAL),
                locCommandeDate,
                locNaissanceDate,
                intV(parStringMapHeaderValue,EnumDataColumImport.P_AGE),
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_AUTRES_INFOS),
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_MESSAGE),
                stringV(parStringMapHeaderValue,EnumDataColumImport.DIOCESE),
                CompareUtils.isMarkTrue(stringV(parStringMapHeaderValue,EnumDataColumImport.P_BESOIN_HEBERGEMENT)),
                CompareUtils.isMarkTrue(stringV(parStringMapHeaderValue,EnumDataColumImport.P_PEUT_HERBERGER)),
                CompareUtils.isMarkTrue(stringV(parStringMapHeaderValue,EnumDataColumImport.P_NE_SOUHAITE_PAS_CHANTER)),
                CompareUtils.isMarkTrue(stringV(parStringMapHeaderValue,EnumDataColumImport.IS_PARTICIPANT_2016)),
                CompareUtils.isMarkTrue(stringV(parStringMapHeaderValue,EnumDataColumImport.AIDE_OFFICES)),
                locHebergement,
                locVoeux,
                locPupitre,
                locChorale,
                locConnaitPar,
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_GROUPE_EVANGELISATION),
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_GROUPE_CONCERT)
        );
    }

    public final List<Participant> getTempToRemovePartList() {
        return _tempToRemovePartList;
    }
}
