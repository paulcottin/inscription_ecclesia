package org.ecclesiacantic.google;

public class GoogleSpreadsheetConfig {

    private final String _configKey, _googleId, _range, _resultCsvFilename;

    public GoogleSpreadsheetConfig(final String parConfigKey, final String parGoogleId,
                                   final String parRange, final String parResultCsvFilename) {
        this._configKey = parConfigKey;
        this._googleId = parGoogleId;
        this._range = parRange;
        this._resultCsvFilename = parResultCsvFilename;
    }

    public final String getConfigKey() {
        return _configKey;
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
