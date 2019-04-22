package org.ecclesiacantic.model.data_manager.bean;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.participant.Chorale;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.ADataManager;
import org.ecclesiacantic.utils.StringUtils;
import org.ecclesiacantic.utils.parser.CsvUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.exception.CsvParseException;
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.io.IOException;
import java.util.*;

public class ChoraleManager extends ADataManager<Chorale> {

    static private ChoraleManager _instance;

    static public final ChoraleManager getInstance() {
        if (_instance == null) {
            _instance = new ChoraleManager();
        }
        return _instance;
    }

    private final Chorale _nullChorale;

    private ChoraleManager() {
        super(EnumConfigProperty.INPUT_F_CHORALE, EnumDataType.CHORALE);
        // Pour les gens qui ne sont pas affiliés à une chorale
        _nullChorale = new Chorale("NO CHORALE REF", true, "-1");;
    }

    private final Chorale getChoraleReferenteForGoupeEvangelisation(final String parGroupeId) {
        for (final Chorale locRefChorale : getChoralesReferentes()) {
            if (locRefChorale.getIdGroupeEvangelisation().equals(parGroupeId)) {
                return locRefChorale;
            }
        }
        System.err.println(String.format("Impossible de trouver de chorale référente pour le groupe d'évangélisation d'id %s",
                parGroupeId));
        return null;
    }

    public final List<Chorale> getChoralesReferentes() {
        final List<Chorale> locReturnList = new ArrayList<>();
        for (final Chorale locChorale : _dataMap.values()) {
            if (locChorale.isReferente()) {
                locReturnList.add(locChorale);
            }
        }
        if (!locReturnList.contains(_nullChorale)) {
            locReturnList.add(_nullChorale);
        }
        return locReturnList;
    }

    private final void processChoralesLinkage() {
        for (final Chorale locChorale : _dataMap.values()) {
            if (locChorale.isReferente()) {
                continue;
            }
            final Chorale locRefChorale = getChoraleReferenteForGoupeEvangelisation(locChorale.getIdGroupeEvangelisation());
            if (locRefChorale != null) {
                locRefChorale.addChoraleFille(locChorale);
            }
        }
    }

    @Override
    public void preProcessingDataFile() {
        CsvUtils.cleanEmptyCsvLines(_propertyDataFile);
        try {
            final Map<String, String> locUniqueColName = new HashMap<>();
            final List<Map<EnumDataColumImport, String>> locData = CsvUtils.parseDataFile(_propertyDataFile, _type.getDataFileHeader());
            for (final Map<EnumDataColumImport, String> locRawData : locData) {
                final String locFirstColName = locRawData.get(EnumDataColumImport.C_NOM);
                if (StringUtils.isNullOrEmpty(locFirstColName)) {
                    continue;
                }
                final String locTransformedName = locFirstColName.replace(" ", "");
                final String locAlreadyComputedName = locUniqueColName.get(locTransformedName);
                if (locAlreadyComputedName == null) {
                    locUniqueColName.put(locTransformedName, locFirstColName);
                } else {
                    locRawData.put(EnumDataColumImport.C_NOM, locAlreadyComputedName);
                }
            }
            CsvUtils.exportFromData(_propertyDataFile, locData);
        } catch (IOException | CsvParseException parE) {
            parE.printStackTrace();
        }
    }

    @Override
    public void postDataIntegration() {
        processChoralesLinkage();
    }

    @Override
    public Chorale getNullData() {
        return _nullChorale;
    }

    @Override
    public void reset() {

    }

    @Override
    protected Chorale convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) throws ObjectInstanciationException {
        final String locGroupeEvangelisationId =
                stringV(parStringMapHeaderValue,EnumDataColumImport.C_ID_GROUPE_EVAN).trim().isEmpty() ?
                        getNullData().getIdGroupeEvangelisation()
                        :
                        stringV(parStringMapHeaderValue,EnumDataColumImport.C_ID_GROUPE_EVAN)
                ;
        return new Chorale(
                stringV(parStringMapHeaderValue,EnumDataColumImport.C_NOM),
                stringV(parStringMapHeaderValue,EnumDataColumImport.C_IS_REFERENCE).equals("X"),
                locGroupeEvangelisationId
        );
    }
}
