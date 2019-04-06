package org.ecclesiacantic.config;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OverrideColumnNameManager {

    static private final String MAPPING = "mapping";
    static private final String REF_NAME = "ref";
    static private final String NEW_NAME = "new";
    static private final String ACTIVE = "active";
    static private final String MAYBE_EMPTY = "maybe_null";

    static private OverrideColumnNameManager _instance;

    static public final OverrideColumnNameManager getInstance() {
        if (_instance == null) {
            _instance = new OverrideColumnNameManager();
        }
        return _instance;
    }

    private final File _mappingFile;

    public OverrideColumnNameManager() {
        _mappingFile = new File(ConfigManager.MAPPING_FILE);
    }

    public final void loadMapping() {
        if (!_mappingFile.exists()) {
            return;
        }
        final String locJsonString = FileUtils.extractString(_mappingFile);
        if (locJsonString == null) {
            throw new IllegalArgumentException("Le fichier de mapping des colonnes est mal formé");
        }
        final JSONObject locRoot = new JSONObject(locJsonString);
        final JSONArray locMappings = locRoot.getJSONArray(MAPPING);
        for (int locIdx = 0; locIdx < locMappings.length(); locIdx++) {
            final JSONObject locMap = locMappings.getJSONObject(locIdx);
            final EnumDataColumImport locColumn = EnumDataColumImport.valueOf(locMap.getString(REF_NAME));
            locColumn.setHeaderName(locMap.getString(NEW_NAME));
            locColumn.setActive(locMap.getBoolean(ACTIVE));
            locColumn.setMaybeEmpty(locMap.has(MAYBE_EMPTY) && locMap.getBoolean(MAYBE_EMPTY));
        }
    }

    public final void saveMapping() {
        final JSONObject locRoot = new JSONObject();
        final JSONArray locMappings = new JSONArray();
        for (final EnumDataColumImport locColumImport : EnumDataColumImport.values()) {
            if (locColumImport.isChanged()) {
                final JSONObject locJSONObject = new JSONObject();
                locJSONObject.put(REF_NAME, locColumImport.name());
                locJSONObject.put(NEW_NAME, locColumImport.getHeaderName());
                locJSONObject.put(ACTIVE, locColumImport.isActive());
                locJSONObject.put(MAYBE_EMPTY, locColumImport.isMaybeEmpty());
                locMappings.put(locJSONObject);
            }
        }
        locRoot.put(MAPPING, locMappings);
        try (final FileWriter locFileWriter = new FileWriter(_mappingFile)) {
            locRoot.write(locFileWriter);
        } catch (IOException parE) {
            parE.printStackTrace();
        }
    }
}
