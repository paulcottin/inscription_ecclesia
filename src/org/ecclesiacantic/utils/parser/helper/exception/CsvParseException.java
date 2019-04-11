package org.ecclesiacantic.utils.parser.helper.exception;

public class CsvParseException extends AParseException {

    public CsvParseException(final String parTypeName, final Exception parCause) {
        super(parTypeName, parCause);
    }

    @Override
    protected String preStandardMsg() {
        return null;
    }

    @Override
    protected String postStandardMsg() {
        return null;
    }
}
