package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.utils.StringUtils;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

public class GoogleRetrieveError extends AParseErrorContent {

    protected final String _httpCode, _checkMessage, _spreadsheetId;

    public GoogleRetrieveError(final String parHttpCode, final String parCheckMessage, final String parSpreadsheetId) {
        _httpCode = parHttpCode;
        _checkMessage = parCheckMessage;
        _spreadsheetId = parSpreadsheetId;
    }

    @Override
    public String getErrorString() {
        if (StringUtils.isNullOrEmpty(_spreadsheetId)) {
            return String.format("Veuillez vérifier que vous avez renseigné l'id Google pour les %s", _typeName);
        } else {
            return String.format("Erreur %s lors du traitement de la feuille d'id '%s'\n%s", _httpCode, _spreadsheetId, _checkMessage);
        }
    }
}
