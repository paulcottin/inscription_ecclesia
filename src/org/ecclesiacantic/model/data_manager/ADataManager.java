package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.google.GoogleSpreadsheetConfig;
import org.ecclesiacantic.google.SpreadSheetDownloader;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.utils.StringUtils;
import org.ecclesiacantic.utils.parser.CsvUtils;
import org.ecclesiacantic.utils.parser.FileUtils;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;
import org.ecclesiacantic.utils.parser.helper.exception.CsvParseException;
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ADataManager<T extends INamedObject > {

    protected final boolean _isDownloadingFiles;
    private final EnumConfigProperty _fileConfigProperty;
    private File _propertyDataFile;
    protected EnumDataType _type;
    protected final Map<String, T> _dataMap;
    protected final String _typeName;
    protected final boolean _isStandaloneDataFile;
    private boolean _dataLoaded, _download;

    public ADataManager(final EnumConfigProperty parPropertyDataFile, final EnumDataType parEnumDataType) {
        this(parPropertyDataFile, parEnumDataType, true);
    }

    public ADataManager(final EnumConfigProperty parPropertyDataFile, final EnumDataType parEnumDataType,
                        final boolean parIsStandaloneDataFile) {
        this._isDownloadingFiles = EnumConfigProperty.RECUP_MODE_GOOGLE.boolV();
        this._fileConfigProperty = parPropertyDataFile;
        this._type = parEnumDataType;
        this._typeName = parEnumDataType.getTypeName();
        _dataMap = new HashMap<>();
        _isStandaloneDataFile = parIsStandaloneDataFile;
        _dataLoaded = false;
        _download = false;
        AllDataManager.getInstance().register(this);
    }

    public abstract void reset();

    protected String stringV(final Map<EnumDataColumImport, String> parDataMap, final EnumDataType parDataType) throws ObjectInstanciationException {
        return stringV(parDataMap, parDataType.getHeaderId());
    }

    protected final Date dateV(final Map<EnumDataColumImport, String> parDataMap, final EnumDataColumImport parColumn, final DateFormat parDateFormat) throws ObjectInstanciationException {
        final String locV = stringV(parDataMap, parColumn);
        if (StringUtils.isNullOrEmpty(locV) && (parColumn.isMaybeEmpty() || !parColumn.isActive())) {
            return new Date();
        }
        try {
            return parDateFormat.parse(locV);
        } catch (final ParseException parE) {
            throw new ObjectInstanciationException(_typeName, parE, parColumn, parDataMap.get(_type.getHeaderId()));
        }
    }

    protected final String stringV(final Map<EnumDataColumImport, String> parDataMap, final EnumDataColumImport parColumn) throws ObjectInstanciationException {
        final String locValue = parDataMap.get(parColumn);
        if (locValue == null) {
            if (!parColumn.isActive() || parColumn.isMaybeEmpty()) {
                return "";
            }
            throw new ObjectInstanciationException(_typeName, parColumn, parDataMap.get(_type.getHeaderId()));
        }
        return locValue;
    }

    protected final int intV(final Map<EnumDataColumImport, String> parDataMap, final EnumDataColumImport parColumn, final int parDefaultValue) {
        try {
            return intV(parDataMap, parColumn);
        } catch (final ObjectInstanciationException parE) {
            return parDefaultValue;
        }
    }

    protected final int intV(final Map<EnumDataColumImport, String> parDataMap, final EnumDataColumImport parColumn) throws ObjectInstanciationException {
        final String locV = stringV(parDataMap, parColumn);
        if (StringUtils.isNullOrEmpty(locV) && (parColumn.isMaybeEmpty() || !parColumn.isActive())) {
            return 0;
        }
        try {
            return Integer.parseInt(locV);
        } catch (final NumberFormatException parE) {
            throw new ObjectInstanciationException(_typeName, parE, parColumn, parDataMap.get(_type.getHeaderId()));
        }
    }

    protected abstract T convertStringMapToObject(final Map<EnumDataColumImport, String> parStringMapHeaderValue) throws AParseException;

    protected void downloadDataFile() throws AParseException {
        final GoogleSpreadsheetConfig locConfig = _type.getGoogleConfig();
        if (locConfig != null) {
            _propertyDataFile = SpreadSheetDownloader.getInstance().downloadSheet(_typeName, locConfig);
            _download = true;
        }
    }

    public void preProcessingDataFile() {

    }

    protected final File propertyDataFile() {
        return _download ? _propertyDataFile : _fileConfigProperty.fileV();
    }

    public final boolean testDataFile() throws AParseException {
        try {
            parseDataFile();
        } finally {
            _dataMap.clear();
            _dataLoaded = false;
            if (propertyDataFile() != null && FileUtils.isFileExist(_propertyDataFile.getParentFile()) && EnumConfigProperty.RECUP_MODE_GOOGLE.boolV()) {
                FileUtils.removeFolder(propertyDataFile().getParentFile());
            }
        }
        return true;
    }

    public boolean parseDataFile() throws AParseException {
        _dataLoaded = true;
        if (_isDownloadingFiles) {
            downloadDataFile();
        }
        preProcessingDataFile();
        try {
            for (final Map<EnumDataColumImport, String> locDataMap : CsvUtils.parseDataFile(propertyDataFile(), _type.getDataFileHeader())) {
                add(convertStringMapToObject(locDataMap));
            }
        } catch (final IOException parE) {
            throw new CsvParseException(_typeName, parE);
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

    public final EnumDataType getType() {
        return _type;
    }
}
