package org.ecclesiacantic.utils.parser.helper;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.error_content.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Collection;

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
            if (locHttpCode == 400) {
                for (final GoogleJsonError.ErrorInfo locErrorInfo : locE.getDetails().getErrors()) {
                    if (locErrorInfo.getMessage().contains("Unable to parse range")) {
                        final String locKey = "range: ";
                        final String locRange = locErrorInfo.getMessage().substring(locErrorInfo.getMessage().indexOf(locKey) + locKey.length());
                        return new GoogleParseError("400", parSpreadsheetId, locRange);
                    }
                }
            }

            throw new IllegalArgumentException(String.format("Unable to find exception mapping for http code %d", locHttpCode), parE);
        }

        throw new IllegalArgumentException(String.format("Unable to retrieve Google exception for %s", parE));
    }

    static public final ObjectInstanciationError computeObjectInstanciation(final Exception parCause,
                                                                            final EnumDataColumImport parColumImport,
                                                                            final String parIdValue,
                                                                            final Collection<String> parAvailableValues) {
        if (parCause == null) {
            return new ObjectInstanciationError(parColumImport, parIdValue, parAvailableValues);
        }

        if (parCause instanceof ParseException && parCause.getMessage().contains("Unparseable date:")) {
            String locDateToParse = parCause.getMessage().replaceFirst("Unparseable date: \"", "");
            final int locLastIdx = locDateToParse.lastIndexOf("\"");
            if (locLastIdx >= 0) {
                locDateToParse = locDateToParse.substring(0, locLastIdx);
            }

            return new DateParseError(parColumImport, parIdValue, locDateToParse);
        }

        if (parCause instanceof NumberFormatException && parCause.getMessage().contains("Unparseable date:")) {
            String locIntToParse = parCause.getMessage().replaceFirst("Unparseable date: \"", "");
            final int locLastIdx = locIntToParse.lastIndexOf("\"");
            if (locLastIdx >= 0) {
                locIntToParse = locIntToParse.substring(0, locLastIdx);
            }

            return new DateParseError(parColumImport, parIdValue, locIntToParse);
        }

        throw new IllegalArgumentException(String.format("Unable to find exception mapping for exception class %s", parCause), parCause);
    }

    static public final AParseErrorContent computeCSV(final Exception parE, final int parLineIdx, final EnumDataColumImport parColumImport, final Collection<String> parAvailableValues) {
        if (parE == null) {
            return new CsvParseError(parLineIdx, parColumImport, parAvailableValues);
        } else if (parE instanceof FileNotFoundException) {
            final String locFilePath = parE.getMessage().substring(0, parE.getMessage().lastIndexOf(" (")).trim();
            return new FileNotFoundError(new File(locFilePath));
        }
        return new CsvColumnEmptyError(parLineIdx, parColumImport);
    }
}
