package org.ecclesiacantic.config;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;
import org.ecclesiacantic.model.data.beans.participant.EnumCivilite;
import org.ecclesiacantic.model.data.beans.participant.EnumPupitre;
import org.ecclesiacantic.model.data.beans.participant.EnumSexeHebergement;
import org.ecclesiacantic.utils.parser.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

public class OverrideColumnNameManager {

    static private final String MAPPING = "mapping";
    static private final String REF_NAME = "ref";
    static private final String NEW_NAME = "new";
    static private final String ACTIVE = "active";
    static private final String MAYBE_EMPTY = "maybe_null";

    static private final String LISTS = "lists";
    static private final String LIST_CLASS = "class";
    static private final String LIST_KEY = "key";
    static private final String LIST_VALUE = "value";

    static private OverrideColumnNameManager _instance;

    static public final OverrideColumnNameManager getInstance() {
        if (_instance == null) {
            _instance = new OverrideColumnNameManager();
        }
        return _instance;
    }

    private final File _mappingFile;
    private final Collection<Class> _toSaveEnums;

    private OverrideColumnNameManager() {
        _mappingFile = new File(ConfigManager.MAPPING_FILE);
        _toSaveEnums = Arrays.asList(EnumPupitre.class, EnumCivilite.class, EnumSexeHebergement.class);
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

        if (locRoot.has(LISTS)) {
            final JSONArray locEnums = locRoot.getJSONArray(LISTS);
            for (int locEnumIdx = 0; locEnumIdx < locEnums.length(); locEnumIdx++) {
                final JSONObject locEnumObject = locEnums.getJSONObject(locEnumIdx);
                final String locEnumClassName = locEnumObject.getString(LIST_CLASS);
                Class locEnumClass = null;
                try {
                    locEnumClass = Class.forName(locEnumClassName);
                } catch (final ClassNotFoundException parE) {
                    parE.printStackTrace();
                }
                if (locEnumClass != null) {
                    final JSONArray locEnumMapping = locEnumObject.getJSONArray(MAPPING);
                    for (int locEnumMapIdx = 0; locEnumMapIdx < locEnumMapping.length(); locEnumMapIdx++) {
                        final JSONObject locMap = locEnumMapping.getJSONObject(locEnumMapIdx);

                        final Enum locEnum = Enum.valueOf(locEnumClass, locMap.getString(LIST_KEY));
                        final ISimpleValueEnum locValueEnum = (ISimpleValueEnum) locEnum;
                        locValueEnum.setValue(locMap.getString(LIST_VALUE));
                    }
                }

            }
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

        final JSONArray locLists = new JSONArray();
        for (final Class locEnumToSave : _toSaveEnums) {
            final JSONObject locEnumObj = new JSONObject();
            final String locClassName = locEnumToSave.getCanonicalName();
            if (locEnumObj.has(locClassName)) {
                continue;
            }
            locEnumObj.put(LIST_CLASS, locClassName);
            final JSONArray locMapping = new JSONArray();
            for (final Enum locEnum : (Iterable<Enum>) EnumSet.allOf(locEnumToSave)) {
                final ISimpleValueEnum locCastedValue = (ISimpleValueEnum) locEnum;
                final JSONObject locJSONObject = new JSONObject();
                locJSONObject.put(LIST_KEY, locEnum.name());
                locJSONObject.put(LIST_VALUE, locCastedValue.getValue());
                locMapping.put(locJSONObject);
            }
            locEnumObj.put(MAPPING, locMapping);
            locLists.put(locEnumObj);
        }
        locRoot.put(LISTS, locLists);
        try (final FileWriter locFileWriter = new FileWriter(_mappingFile)) {
            locRoot.write(locFileWriter);
        } catch (IOException parE) {
            parE.printStackTrace();
        }
    }

    public final Collection<Class> getToSaveEnums() {
        return _toSaveEnums;
    }
}
