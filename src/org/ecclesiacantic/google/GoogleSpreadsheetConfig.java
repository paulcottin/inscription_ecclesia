package org.ecclesiacantic.google;

import org.ecclesiacantic.config.EnumConfigProperty;

public class GoogleSpreadsheetConfig {

    private final String _configKey, _dataRangeKey, _resultFileNameKey;
    private final String _googleId, _range, _resultCsvFilename;

    public GoogleSpreadsheetConfig(final String parConfigKey, final String parDataRangeKey, final String parResultFileNameKey) {
        this._configKey = parConfigKey;
        this._dataRangeKey = parDataRangeKey;
        this._resultFileNameKey = parResultFileNameKey;
        this._googleId = EnumConfigProperty.property(_configKey).stringV();
        this._range = EnumConfigProperty.property(_dataRangeKey).stringV();
        this._resultCsvFilename = EnumConfigProperty.property(_resultFileNameKey).stringV();
    }

    public final String getConfigKey() {
        return _configKey;
    }

    public final String getDataRangeKey() {
        return _dataRangeKey;
    }

    public final String getResultFileNameKey() {
        return _resultFileNameKey;
    }

    public final String getGoogleId() {
        return _googleId;
    }

    public final String getRange() {
        return _range;
    }

    public final String getResultCsvFilename() {
        return _resultCsvFilename;
    }
}
