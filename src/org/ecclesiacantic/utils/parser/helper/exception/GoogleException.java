package org.ecclesiacantic.utils.parser.helper.exception;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;
import org.ecclesiacantic.utils.parser.helper.ParseErrorFactory;

public class GoogleException extends AParseException {

    private final String _spreadsheetId;

    public GoogleException(final String parTypeName, final String parSpreadsheetId, final Exception parCause) {
        super(parTypeName, parCause);
        _spreadsheetId = parSpreadsheetId;

    }

    @Override
    protected AParseErrorContent computeContent(final Exception parCause) {
        return ParseErrorFactory.computeGoogle(parCause, _spreadsheetId);
    }
}
