package org.ecclesiacantic.config;

import org.ecclesiacantic.google.GoogleSpreadsheetConfig;
import org.ecclesiacantic.utils.parser.NumberUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum EnumConfigProperty {

    INPUT_F_MC("input.file.masterclass"),
    INPUT_F_SALLE("input.file.salles"),
    INPUT_F_PARTICIPANT("input.file.participants"),
    INPUT_F_CHORALE("input.file.chorales"),
    INPUT_F_SOLO_GEO("input.file.solo_geographique"),
    INPUT_F_GROUP_CONCERT("input.file.groupe_concert"),
    OUTPUT_F_SALLE("output.file.salle_mc"),
    OUTPUT_F_BADGE("output.file.badges"),
    OUTPUT_F_G_EVAN("output.file.groupe_evangelisation"),
    OUTPUT_F_G_CONCERT("output.file.groupe_concert"),
    OUTPUT_F_MAILING_L("output.file.mailing_list"),
    OUTPUT_F_NB_PART_BY_CRENEAU("output.file.nb_part_by_mc_and_creneaux"),
    OUTPUT_F_G_EVAN_PART_RELATION("output.file.ge_participants_relation"),
    OUTPUT_D_MC_CHECKLIST("output.dir.masterclass_checklist"),
    BOOLEAN_MARK("input.value.boolean_marker"),
    NB_MARGE_PLACE_SALLE("input.value.nb_places_marge_salle"),
    NB_MAX_VOEUX("input.value.max_participant_voeux"),
    NB_VOEUX_TO_CONSIDER("input.value.nb_voeu_to_consider"),
    LESS_POP_MC_NB("input.value.less_popular_mc_number", true),
    HUGE_SALLE_NB("input.value.huge_salles_mc_number", true),
    MIN_PART_NB_MC("input.value.min_part_number_for_mc", true),
    MIN_FULL_FACTOR("input.value.min_full_factor", true),
    MAX_FULL_FACTOR("input.value.max_full_factor", true),
    LESS_USED_PART("input.value.min_lessUsedPart", true),
    MAX_USED_PART("input.value.max_lessUsedPart", true),
    IS_SKIP_MALFORMED_VOEUX("input.value.skip_malformed_voeu_repartition", true),
    IS_COMPUTE_GE_REPART("input.value.compute_groupe_evangelisation_repartition", true),
    IS_USE_SORTED_PARTICIPANTS("input.value.use_sorted_participant_list"),
    NB_VOEUX_CONSIDERED("input.value.nb_voeux_considered"),
    API_EMAIL("input.google.api_email"),
    RECUP_MODE_GOOGLE("input.mode.download_google", true),
    G_PART_ID("participant", EnumConfigProperty.GOOGLE_ID),
    G_PART_DATA_RANGE("participant", EnumConfigProperty.GOOGLE_DATA_RANGE),
    G_PART_DATA_RESULT_F("participant", EnumConfigProperty.GOOGLE_RF_NAME),
    G_MC_ID("masterclasse", EnumConfigProperty.GOOGLE_ID),
    G_MC_DATA_RANGE("masterclasse", EnumConfigProperty.GOOGLE_DATA_RANGE),
    G_MC_DATA_RESULT_F("masterclasse", EnumConfigProperty.GOOGLE_RF_NAME),
    G_SALLE_ID("salle", EnumConfigProperty.GOOGLE_ID),
    G_SALLE_RANGE("salle", EnumConfigProperty.GOOGLE_DATA_RANGE),
    G_SALLE_RESULT_F("salle", EnumConfigProperty.GOOGLE_RF_NAME),
    G_CHORALE_ID("chorale", EnumConfigProperty.GOOGLE_ID),
    G_CHORALE_RANGE("chorale", EnumConfigProperty.GOOGLE_DATA_RANGE),
    G_CHORALE_RESULT_F("chorale", EnumConfigProperty.GOOGLE_RF_NAME),
    G_SOLO_G_ID("solo_geographique", EnumConfigProperty.GOOGLE_ID),
    G_SOLO_G_RANGE("solo_geographique", EnumConfigProperty.GOOGLE_DATA_RANGE),
    G_SOLO_G_RESULT_F("solo_geographique", EnumConfigProperty.GOOGLE_RF_NAME),
    G_GROUPE_C_ID("groupe_concert", EnumConfigProperty.GOOGLE_ID),
    G_GROUPE_C_RANGE("groupe_concert", EnumConfigProperty.GOOGLE_DATA_RANGE),
    G_GROUPE_C_RESULT_F("groupe_concert", EnumConfigProperty.GOOGLE_RF_NAME)
    ;

    static private final String GOOGLE_INPUT_PREFIX = "input.google";
    static private final String GOOGLE_ID = "google_id";
    static private final String GOOGLE_DATA_RANGE = "data_range";
    static private final String GOOGLE_RF_NAME = "result_file_name";

    private String _key;
    private String _googleKey;
    private boolean _googleProperty;
    private boolean _saveOnGuiChange;

    EnumConfigProperty(final String parKey) {
        this(parKey, false);
    }

    EnumConfigProperty(final String parKey, final boolean parSaveOnGuiChange) {
        this(parKey, false, parSaveOnGuiChange);
    }

    EnumConfigProperty(final String parInputGoogleObjType, final String parKey) {
        this(parInputGoogleObjType, parKey, false);
    }

    EnumConfigProperty(final String parInputGoogleObjType, final String parKey, final boolean parSaveOnGuiChange) {
        this(String.format("%s.%s.%s", GOOGLE_INPUT_PREFIX, parInputGoogleObjType, parKey), true, parSaveOnGuiChange);
        _googleKey = parInputGoogleObjType;
    }

    EnumConfigProperty(final String parKey, final boolean parIsGoogleProperty, final boolean parSaveOnGuiChange) {
        _key = parKey;
        _googleKey = null;
        _googleProperty = parIsGoogleProperty;
        _saveOnGuiChange = parSaveOnGuiChange;
    }

    static public final EnumConfigProperty property(final String parProperty) {
        for (final EnumConfigProperty locProp : values()) {
            if (locProp.getKey().equals(parProperty)) {
                return locProp;
            }
        }
        System.err.println(String.format("Impossible de trouver la configuration pour la clef %s", parProperty));
        return null;
    }

    static public final List<EnumConfigProperty> googleIdProperties() {
        final List<EnumConfigProperty> locReturnList = new ArrayList<>();
        for (final EnumConfigProperty locProp : values()) {
            if (locProp.isGoogleProperty() && locProp.getKey().contains(GOOGLE_ID)) {
                locReturnList.add(locProp);
            }
        }
        return locReturnList;
    }

    static public final GoogleSpreadsheetConfig googleConfigV(final EnumConfigProperty parIdProperty) {
        if (!parIdProperty.isGoogleProperty()) {
            return null;
        }
        return new GoogleSpreadsheetConfig(
                parIdProperty.getKey(),
                String.format("%s.%s.%s", GOOGLE_INPUT_PREFIX, parIdProperty.getGoogleKey(), GOOGLE_DATA_RANGE),
                String.format("%s.%s.%s", GOOGLE_INPUT_PREFIX, parIdProperty.getGoogleKey(), GOOGLE_RF_NAME)
        );
    }

    public final int intV() {
        return NumberUtils.convertFieldToInt(ConfigManager.getInstance().getProperty(_key));
    }

    public final double doubleV() {return NumberUtils.convertFieldToDouble(ConfigManager.getInstance().getProperty(_key));}

    public final boolean boolV() {
        return "1".equals(ConfigManager.getInstance().getProperty(_key));
    }

    public final String stringV() {
        return ConfigManager.getInstance().getProperty(_key);
    }

    public final File fileV() {
        final String locFilePath = stringV();
        return locFilePath == null ? null : new File(locFilePath);
    }

    public boolean isGoogleProperty() {
        return _googleProperty;
    }

    public final String getKey() {
        return _key;
    }

    public final String getGoogleKey() {
        return _googleKey;
    }

    public boolean isSaveOnGuiChange() {
        return _saveOnGuiChange;
    }

    @Override
    public String toString() {
        return getKey();
    }

    public void setKey(final String parKey) {
        _key = parKey;
    }

    public void setGoogleKey(final String parGoogleKey) {
        _googleKey = parGoogleKey;
    }

    public void setGoogleProperty(final boolean parGoogleProperty) {
        _googleProperty = parGoogleProperty;
    }
}
