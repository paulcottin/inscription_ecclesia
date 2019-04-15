package org.ecclesiacantic.utils.parser.helper.error_content;

public class GoogleParseError extends GoogleRetrieveError {

    private final String _range;

    public GoogleParseError(final String parHttpCode, final String parSpreadsheetId, final String parRange) {
        super(parHttpCode, null, parSpreadsheetId);
        _range = parRange;
    }

    @Override
    public String getErrorString() {
        return String.format("Impossible de trouver le groupe de cellules \"%s\" dans la feuille Google d'id \"%s\"", _range, _spreadsheetId);
    }
}
