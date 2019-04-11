package org.ecclesiacantic.utils.parser.helper;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class ParseErrorFactory {

    static public final AParseErrorContent computeError(final Exception parE) {
        assert parE != null;
        if (parE instanceof GoogleJsonResponseException) {
            return computeGoogle((GoogleJsonResponseException) parE);
        }
        throw new IllegalArgumentException(String.format("Unable to deal with exception type %s", parE.getClass()), parE);
    }

    static private final GoogleRetrieveError computeGoogle(final GoogleJsonResponseException parE) {
        final int locHttpCode = parE.getStatusCode();
        if (locHttpCode == 404) {
            return new GoogleRetrieveError("404", "Veuillez vérifier que la feuille Google existe bien");
        }

        throw new IllegalArgumentException(String.format("Unable to find exception mapping for http code %d", locHttpCode), parE);
    }
}
