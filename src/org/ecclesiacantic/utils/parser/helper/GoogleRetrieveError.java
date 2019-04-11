package org.ecclesiacantic.utils.parser.helper;

public class GoogleRetrieveError extends AParseErrorContent {

    private final String _httpCode, _checkMessage;

    GoogleRetrieveError(final String parHttpCode, final String parCheckMessage) {
        _httpCode = parHttpCode;
        _checkMessage = parCheckMessage;
    }

    @Override
    public String getErrorString() {
        return String.format("Erreur %s lors de la connexion à Google : %s", _httpCode, _checkMessage);
    }
}
