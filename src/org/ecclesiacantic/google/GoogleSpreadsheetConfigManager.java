package org.ecclesiacantic.google;

import org.ecclesiacantic.config.EnumConfigProperty;

import java.util.HashMap;
import java.util.Map;

public class GoogleSpreadsheetConfigManager {

    static private GoogleSpreadsheetConfigManager _instance;

    static public final GoogleSpreadsheetConfigManager getInstance() {
        if (_instance == null) {
            _instance = new GoogleSpreadsheetConfigManager();
            _instance.initConfigs();
        }
        return _instance;
    }

    private final Map<String, GoogleSpreadsheetConfig> _configs;

    public GoogleSpreadsheetConfigManager() {
        _configs = new HashMap<>();
    }

    private final void initConfigs() {
        for (final EnumConfigProperty locGoogleProp : EnumConfigProperty.googleIdProperties()) {
            _configs.put(locGoogleProp.getGoogleKey(), EnumConfigProperty.googleConfigV(locGoogleProp));
        }
    }

    public final GoogleSpreadsheetConfig get(final String parDataTypeKey) {
        if (_configs.containsKey(parDataTypeKey)) {
            return _configs.get(parDataTypeKey);
        }
        return null;
    }
}
