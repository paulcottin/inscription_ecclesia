package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.google.GoogleSpreadsheetConfig;
import org.ecclesiacantic.google.SpreadSheetDownloader;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.utils.parser.CsvUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ADataManager<T extends INamedObject > {

    protected final boolean _isDownloadingFiles;
    protected File _propertyDataFile;
    protected EnumDataType _type;
    protected final Map<String, T> _dataMap;
    protected final String _typeName;
    protected final boolean _isStandaloneDataFile;
    private boolean _dataLoaded;

    public ADataManager(final EnumConfigProperty parPropertyDataFile, final EnumDataType parEnumDataType) {
        this(parPropertyDataFile, parEnumDataType, true);
    }

    public ADataManager(final EnumConfigProperty parPropertyDataFile, final EnumDataType parEnumDataType,
                        final boolean parIsStandaloneDataFile) {
        this._isDownloadingFiles = EnumConfigProperty.RECUP_MODE_GOOGLE.boolV();
        this._propertyDataFile = parPropertyDataFile.fileV();
        this._type = parEnumDataType;
        this._typeName = parEnumDataType.getTypeName();
        _dataMap = new HashMap<>();
        _isStandaloneDataFile = parIsStandaloneDataFile;
        _dataLoaded = false;
        AllDataManager.getInstance().register(this);
    }

    public abstract void reset();

    protected abstract T convertStringMapToObject(final Map<EnumDataColumImport, String> parStringMapHeaderValue);

    protected void downloadDataFile() throws AParseException {
        final GoogleSpreadsheetConfig locConfig = _type.getGoogleConfig();
        if (locConfig != null) {
            _propertyDataFile = SpreadSheetDownloader.getInstance().downloadSheet(_typeName, locConfig);
        }
    }

    public void preProcessingDataFile() {

    }

    public final boolean testDataFile() {
        boolean locTestResult = false;
        try {
            locTestResult = parseDataFile();
        } catch (final AParseException parE) {
            parE.printStackTrace();
        }
        _dataMap.clear();
        _dataLoaded = false;
        return locTestResult;
    }

    public boolean parseDataFile() throws AParseException {
        _dataLoaded = true;
        if (_isDownloadingFiles) {
            downloadDataFile();
        }
        preProcessingDataFile();
        try {
            for (final Map<EnumDataColumImport, String> locDataMap : CsvUtils.parseDataFile(_propertyDataFile, _type.getDataFileHeader())) {
                add(convertStringMapToObject(locDataMap));
            }
        } catch (final IOException parE) {
            parE.printStackTrace();
            return false;
        }
        postDataIntegration();
        return true;
    }

    /**
     * Permet d'effectuer des traitements pour certains types de données après l'intégration des
     * données brutes via fichiers
     */
    public void postDataIntegration() {

    }

    public void add(final T parObject) {
        parseFileIfNSecure();
        if (parObject != null && !_dataMap.keySet().contains(parObject.getName())) {
            _dataMap.put(parObject.getName(), parObject);
        } else {
            if (_isStandaloneDataFile) {
                System.err.println(String.format(
                        "L'objet %s inséré est null ou est déjà présent : %s",
                        _typeName,
                        parObject != null ? parObject.getName() : "null"
                ));
            } else {
                //Ne rien n'afficher, on se base sur un autre fichier pour calculer ces données,
                // C'est normal qu'il y ait des doublons
            }
        }
    }

    private final void parseFileIfN() throws AParseException {
        if (!_dataLoaded) {
            parseDataFile();
        }
    }

    private final void parseFileIfNSecure() {
        if (!_dataLoaded) {
            try {
                parseFileIfN();
            } catch (final AParseException parE) {
                parE.printStackTrace();
            }
        }
    }

    public final boolean exists(final String parObjectName) {
        parseFileIfNSecure();
        if (parObjectName != null) {
            return _dataMap.keySet().contains(parObjectName);
        } else {
            System.err.println(String.format("L'objet %s demandé est null", _typeName));
            return false;
        }
    }

    public final T get(final String parObjectName) {
        parseFileIfNSecure();
        if (parObjectName != null) {
            if (_dataMap.keySet().contains(parObjectName)) {
                return _dataMap.get(parObjectName);
            } else {
                final T locNullData = getNullData();
                if (locNullData != null) {
                    // On autorise les valeurs vides pour ce type de données
                    return locNullData;
                } else {
                    System.err.println(String.format("Impossible de trouver l'objet %s %s",_typeName,  parObjectName));
                    return null;
                }
            }
        } else {
            System.err.println(String.format("L'objet %s demandé est null", _typeName));
            return null;
        }
    }

    /**
     * Si la fonction ne retourne pas une valeur null c'est qu'on accepte les valeurs vides
     * pour ce type de données
     * @return
     */
    public T getNullData() {
        return null;
    }

    public final Collection<T> getAllData() {
        parseFileIfNSecure();
        return _dataMap.values();
    }

    public final File getPropertyDataFile() {
        return _propertyDataFile;
    }

    public final EnumDataType getType() {
        return _type;
    }
}
