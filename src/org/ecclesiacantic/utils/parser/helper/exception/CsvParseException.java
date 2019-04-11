package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

public class CsvParseException extends AParseException {

    public CsvParseException(final String parTypeName, final Exception parCause) {
        super(parTypeName, parCause);
    }

    @Override
    protected AParseErrorContent computeContent(final Exception parCause) {
        return null;
    }
}
