package org.ecclesiacantic.utils.parser.helper;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.error_content.DateParseError;
import org.ecclesiacantic.utils.parser.helper.error_content.GoogleRetrieveError;
import org.ecclesiacantic.utils.parser.helper.error_content.ObjectInstanciationError;

import java.net.UnknownHostException;
import java.text.ParseException;

public class ParseErrorFactory {

    static public final GoogleRetrieveError computeGoogle(final Exception parE, final String parSpreadsheetId) {
        if (parE instanceof UnknownHostException) {
            return new GoogleRetrieveError("0", "Impossible de contacter Google, vérifiez que vous êtes connecté à Internet", parSpreadsheetId);
        }

        if (parE instanceof GoogleJsonResponseException) {
            final GoogleJsonResponseException locE = (GoogleJsonResponseException) parE;
            final int locHttpCode = locE.getStatusCode();
            if (locHttpCode == 404) {
                return new GoogleRetrieveError("404", "Veuillez vérifier que la feuille Google existe bien", parSpreadsheetId);
            }

            throw new IllegalArgumentException(String.format("Unable to find exception mapping for http code %d", locHttpCode), parE);
        }

        throw new IllegalArgumentException(String.format("Unable to retrieve Google exception for %s", parE));
    }

    static public final ObjectInstanciationError computeObjectInstanciation(final Exception parCause, final EnumDataColumImport parColumImport, final String parIdValue) {
        if (parCause == null) {
            return new ObjectInstanciationError(parColumImport, parIdValue);
        }

        if (parCause instanceof ParseException && parCause.getMessage().contains("Unparseable date:")) {
            String locDateToParse = parCause.getMessage().replaceFirst("Unparseable date: \"", "");
            final int locLastIdx = locDateToParse.lastIndexOf("\"");
            if (locLastIdx >= 0) {
                locDateToParse = locDateToParse.substring(0, locLastIdx);
            }

            return new DateParseError(parColumImport, parIdValue, locDateToParse);
        }

        throw new IllegalArgumentException(String.format("Unable to find exception mapping for exception class %s", parCause), parCause);
    }
}
