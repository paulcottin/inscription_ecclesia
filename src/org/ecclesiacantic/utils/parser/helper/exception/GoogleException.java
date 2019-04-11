package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.utils.StringUtils;

public class GoogleException extends AParseException {

    private final String _spreadsheetId;

    public GoogleException(final String parTypeName, final String parSpreadsheetId, final Exception parCause) {
        super(parTypeName, parCause);
        _spreadsheetId = parSpreadsheetId;

    }

    @Override
    protected String preStandardMsg() {
        if (StringUtils.isNullOrEmpty(_spreadsheetId)) {
            return String.format("Veuillez vérifier que vous avez renseigné l'id Google pour les %s", _typeName);
        } else {
            return String.format("Erreur lors du traitement de la feuille d'id '%s'", _spreadsheetId);
        }
    }

    @Override
    protected String postStandardMsg() {
        return null;
    }
}
